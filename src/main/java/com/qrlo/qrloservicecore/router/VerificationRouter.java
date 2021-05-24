package com.qrlo.qrloservicecore.router;

import com.qrlo.qrloservicecore.handler.VerificationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-24
 */
@Configuration
public class VerificationRouter {
    @Bean
    public RouterFunction<ServerResponse> verificationRoutes(VerificationHandler verificationHandler) {
        return RouterFunctions
                .nest(path("/verification"),
                        RouterFunctions
                                .route(GET("/accounts/{token}"), verificationHandler::verifyAccountActivationToken)
                                .andRoute(GET("/businesscards/{token}"), verificationHandler::verifyBusinessCardActivationToken));
    }
}
