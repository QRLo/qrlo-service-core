package com.qrlo.qrloservicecore.router;

import com.qrlo.qrloservicecore.handler.ProfileHandler;
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
public class ProfileRouter {
    @Bean
    public RouterFunction<ServerResponse> profileRoutes(ProfileHandler profileHandler) {
        return RouterFunctions
                .route(GET("/profile"), profileHandler::getProfile)
                .andRoute(PUT("/profile"), profileHandler::updateProfile)
                .andNest(path("/profile"),
                        RouterFunctions
                                .route(GET("/mybusinesscards"), profileHandler::getAllBusinessCards)
                                .andRoute(POST("/mybusinesscards"), profileHandler::addMyBusinessCard)
                                .andNest(path("/mybusinesscards"),
                                        RouterFunctions.route(GET("/{id}/generate-qr"), profileHandler::getMyBusinessCardQr)));
    }
}
