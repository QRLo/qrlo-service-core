package com.qrlo.qrloservicecore.profile;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-22
 */
@Configuration
public class ProfileRouter {
    @Bean
    public RouterFunction<ServerResponse> profileRoutes(ProfileHandler profileHandler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/profile"), profileHandler::getProfile);
    }
}
