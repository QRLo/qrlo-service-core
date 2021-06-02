package com.qrlo.qrloservicecore.handler.profile;

import com.qrlo.qrloservicecore.util.RequestUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-29
 */
@Component
public class ProfileContactHandler {
    public Mono<ServerResponse> addContact(ServerRequest request) {
        return RequestUtils
                .getUserIdFromRequestPrincipal(request)
                .then(ServerResponse.ok().build());
    }
}
