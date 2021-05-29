package com.qrlo.qrloservicecore.handler;

import com.qrlo.qrloservicecore.handler.domain.HealthCheckResponse;
import com.qrlo.qrloservicecore.handler.domain.HealthStatus;
import com.qrlo.qrloservicecore.util.RequestUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-22
 */
@Component
public class HealthHandler {
    public Mono<ServerResponse> healthCheck(ServerRequest request) {
        return RequestUtils
                .getUserIdFromRequestPrincipal(request)
                .map(id -> new HealthCheckResponse(HealthStatus.UP, id != null))
                .flatMap(healthCheckResponse -> ServerResponse.ok().bodyValue(healthCheckResponse));
    }
}
