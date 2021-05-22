package com.qrlo.qrloservicecore.handler;

import com.qrlo.qrloservicecore.model.BusinessCard;
import com.qrlo.qrloservicecore.model.User;
import com.qrlo.qrloservicecore.service.BusinessCardService;
import com.qrlo.qrloservicecore.service.QrCodeService;
import com.qrlo.qrloservicecore.service.UserService;
import com.qrlo.qrloservicecore.service.VCardService;
import com.qrlo.qrloservicecore.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public ProfileHandler(UserService userService, BusinessCardService businessCardService, QrCodeService qrCodeService,
                          VCardService vCardService) {
        this.userService = userService;
        this.businessCardService = businessCardService;
        this.qrCodeService = qrCodeService;
        this.vCardService = vCardService;
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
        return request
                .bodyToMono(BusinessCard.class)
                .zipWith(RequestUtils.getUserIdFromRequest(request))
                .flatMap(t -> businessCardService.saveBusinessCardForUser(t.getT1(), t.getT2()))
                .flatMap(businessCard -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(businessCard));
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
