package com.qrlo.qrloservicecore.router;

import com.qrlo.qrloservicecore.handler.AuthHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
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
                .route(POST("/auth")
                        .and(accept(MediaType.APPLICATION_JSON)), authHandler::authenticate)
                .andNest(path("/auth"), RouterFunctions
                        .route(POST("/integrate"), authHandler::integrateOAuth)
                        .andRoute(GET("/verify/{token}"), authHandler::verifyActivationToken));
    }
}
