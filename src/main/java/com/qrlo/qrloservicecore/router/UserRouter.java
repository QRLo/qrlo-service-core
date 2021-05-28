package com.qrlo.qrloservicecore.router;

import com.qrlo.qrloservicecore.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-28
 */
@Configuration
public class UserRouter {
    @Bean
    public static RouterFunction<ServerResponse> userRoutes(UserHandler userHandler) {
        return RouterFunctions
                .route(GET("/users/{userId}/businesscards/{businessCardId}"), userHandler::getBusinessCard);
    }
}
