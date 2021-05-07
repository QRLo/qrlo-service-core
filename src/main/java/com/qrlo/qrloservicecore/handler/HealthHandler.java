package com.qrlo.qrloservicecore.handler;

import com.qrlo.qrloservicecore.handler.domain.HealthCheckResponse;
import com.qrlo.qrloservicecore.handler.domain.HealthStatus;
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
        HealthCheckResponse healthCheckResponse = new HealthCheckResponse(HealthStatus.UP);
        return ServerResponse.ok().body(Mono.just(healthCheckResponse), HealthCheckResponse.class);
    }
}
