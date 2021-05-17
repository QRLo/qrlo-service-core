package com.qrlo.qrloservicecore.handler;

import com.qrlo.qrloservicecore.model.BusinessCard;
import com.qrlo.qrloservicecore.security.JwtTokenProvider;
import com.qrlo.qrloservicecore.service.BusinessCardService;
import com.qrlo.qrloservicecore.service.QrCodeService;
import com.qrlo.qrloservicecore.util.RequestUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-06
 */
@Component
public class BusinessCardHandler {
    private final BusinessCardService businessCardService;
    private final QrCodeService qrCodeService;
    private final JwtTokenProvider jwtTokenProvider;

    public BusinessCardHandler(BusinessCardService businessCardService, QrCodeService qrCodeService, JwtTokenProvider jwtTokenProvider) {
        this.businessCardService = businessCardService;
        this.qrCodeService = qrCodeService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Mono<ServerResponse> verifyBusinessCard(ServerRequest request) {
        String token = request.pathVariable("token");
        return Mono.empty();
    }

    public Mono<ServerResponse> createBusinessCard(ServerRequest request) {
        Mono<String> userIdMono = RequestUtils
                .getUserIdFromRequest(request).cache();
        return request
                .bodyToMono(BusinessCard.class)
                .zipWith(userIdMono, businessCardService::saveBusinessCardForUser)
                .flatMap(businessCardMono -> ServerResponse.ok().body(businessCardMono, BusinessCard.class));
    }

    public Mono<ServerResponse> generateQrCode(ServerRequest request) {
        return qrCodeService.generateQrCode("Test")
                .flatMap(bytes -> ServerResponse.ok().contentType(MediaType.IMAGE_PNG).bodyValue(bytes));
    }
}
