package com.qrlo.qrloservicecore.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-21
 */
@Configuration
public class AuthRouter {
    @Bean
    public RouterFunction<ServerResponse> authRoutes(AuthHandler authHandler) {
        return RouterFunctions
                .route(POST("/auth"), authHandler::authenticate);
    }
}
