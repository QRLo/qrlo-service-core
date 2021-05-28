package com.qrlo.qrloservicecore.handler;

import com.qrlo.qrloservicecore.service.BusinessCardService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-28
 */
@Component
public class UserHandler {
    private final BusinessCardService businessCardService;

    public UserHandler(BusinessCardService businessCardService) {
        this.businessCardService = businessCardService;
    }

    public Mono<ServerResponse> getBusinessCard(ServerRequest request) {
        int userId = Integer.parseInt(request.pathVariable("userId"));
        int businessCardId = Integer.parseInt(request.pathVariable("businessCardId"));
        return businessCardService.getBusinessCardForUserById(businessCardId, userId)
                .flatMap(userBusinessCard -> ServerResponse.ok().bodyValue(userBusinessCard));
    }
}
