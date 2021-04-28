package com.qrlo.qrloservicecore.health;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-22
 */
@Configuration
public class HealthRouter {
    @Bean
    public RouterFunction<ServerResponse> healthRoutes(HealthHandler healthHandler) {
        return RouterFunctions.route(GET("health"), healthHandler::healthCheck);
    }
}
