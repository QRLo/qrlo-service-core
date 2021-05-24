package com.qrlo.qrloservicecore.handler;

import com.qrlo.qrloservicecore.model.BusinessCard;
import com.qrlo.qrloservicecore.model.User;
import com.qrlo.qrloservicecore.service.*;
import com.qrlo.qrloservicecore.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-22
 */
@Slf4j
@Component
public class ProfileHandler {
    private final UserService userService;
    private final BusinessCardService businessCardService;
    private final QrCodeService qrCodeService;
    private final VCardService vCardService;
    private final EmailService emailService;

    public ProfileHandler(UserService userService, BusinessCardService businessCardService, QrCodeService qrCodeService,
                          VCardService vCardService, EmailService emailService) {
        this.userService = userService;
        this.businessCardService = businessCardService;
        this.qrCodeService = qrCodeService;
        this.vCardService = vCardService;
        this.emailService = emailService;
    }

    public Mono<ServerResponse> getProfile(ServerRequest request) {
        return RequestUtils.getUserIdFromRequest(request)
                .flatMap(userService::findById)
                .flatMap(user -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(user));
    }

    public Mono<ServerResponse> updateProfile(ServerRequest request) {
        return request
                .bodyToMono(User.class)
                .flatMap(userService::saveUser)
                .flatMap(user -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(user));
    }

    public Mono<ServerResponse> addMyBusinessCard(ServerRequest request) {
        Mono<Integer> userIdMono = RequestUtils.getUserIdFromRequest(request).cache();
        Mono<BusinessCard> createdBusinessCardMono = request
                .bodyToMono(BusinessCard.class)
                .zipWith(userIdMono)
                .flatMap(t -> businessCardService.saveBusinessCardForUser(t.getT1(), t.getT2()))
                .cache();
        return userIdMono
                .flatMap(userService::findById)
                .zipWith(createdBusinessCardMono)
                .doOnNext(t -> emailService.sendBusinessCardVerificationEmail(t.getT1(), t.getT2()).subscribe())
                .doOnNext(businessCard -> log.info("Sending created business card: {}", businessCard.toString()))
                .then(createdBusinessCardMono)
                .flatMap(createdBusinessCard -> ServerResponse.ok().bodyValue(createdBusinessCard));
    }

    public Mono<ServerResponse> getAllBusinessCards(ServerRequest request) {
        return RequestUtils.getUserIdFromRequest(request)
                .flatMapMany(businessCardService::getAllBusinessCardsByUserId)
                .collectList()
                .flatMap(businessCards -> ServerResponse.ok().bodyValue(businessCards));
    }

    public Mono<ServerResponse> getMyBusinessCardQr(ServerRequest request) {
        int businessCardId = Integer.parseInt(request.pathVariable("id"));
        return RequestUtils
                .getUserIdFromRequest(request)
                .flatMap(userId -> businessCardService
                        .getBusinessCardForUserById(businessCardId, userId))
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Business card for user is not found"))))
                .flatMap(vCardService::generateFromUserBusinessCard)
                .flatMap(vCardService::writeVCardAsString)
                .flatMap(qrCodeService::generateQrCode)
                .flatMap(imageBytes -> ServerResponse.ok().contentType(MediaType.IMAGE_PNG).bodyValue(imageBytes));
    }
}
