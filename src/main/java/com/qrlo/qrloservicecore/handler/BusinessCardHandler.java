package com.qrlo.qrloservicecore.handler;

import com.qrlo.qrloservicecore.model.BusinessCard;
import com.qrlo.qrloservicecore.config.security.JwtTokenProvider;
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

    public BusinessCardHandler(BusinessCardService businessCardService) {
        this.businessCardService = businessCardService;
    }

    public Mono<ServerResponse> getBusinessCardById(ServerRequest request) {
        int id = Integer.parseInt(request.pathVariable("id"));
        return businessCardService.getBusinessCardForUserById(id)
                .flatMap(userBusinessCard -> ServerResponse.ok().bodyValue(userBusinessCard));
    }
}
