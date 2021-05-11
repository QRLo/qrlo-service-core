package com.qrlo.qrloservicecore.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qrlo.qrloservicecore.model.BusinessCard;
import com.qrlo.qrloservicecore.model.User;
import com.qrlo.qrloservicecore.service.BusinessCardService;
import com.qrlo.qrloservicecore.service.QrCodeService;
import com.qrlo.qrloservicecore.service.UserService;
import com.qrlo.qrloservicecore.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
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

    public ProfileHandler(UserService userService, BusinessCardService businessCardService, QrCodeService qrCodeService) {
        this.userService = userService;
        this.businessCardService = businessCardService;
        this.qrCodeService = qrCodeService;
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
                .flatMap(t -> userService.addMyBusinessCard(t.getT2(), t.getT1()))
                .flatMap(businessCard -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(businessCard));
    }

    public Mono<ServerResponse> getMyBusinessCardQr(ServerRequest request) {
        String id = request.pathVariable("id");
        return RequestUtils
                .getUserIdFromRequest(request)
                .flatMap(userId -> businessCardService.getUnwoundBusinessCardForUserById(id, userId))
                .flatMap(unwoundUserBusinessCard -> Mono.fromCallable(() -> new ObjectMapper().writeValueAsString(unwoundUserBusinessCard)))
                .flatMap(qrCodeService::generateQrCode)
                .flatMap(imageBytes -> ServerResponse.ok().contentType(MediaType.IMAGE_PNG).bodyValue(imageBytes));
    }
}
