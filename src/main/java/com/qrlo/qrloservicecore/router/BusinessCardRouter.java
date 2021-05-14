package com.qrlo.qrloservicecore.router;

import com.qrlo.qrloservicecore.handler.BusinessCardHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-06
 */
@Configuration
public class BusinessCardRouter {
    @Bean
    public static RouterFunction<ServerResponse> businessCardRoutes(BusinessCardHandler businessCardHandler) {
        return RouterFunctions
                .route(POST("/businesscards"), businessCardHandler::createBusinessCard)
                .andRoute(GET("/businesscards/generate"), businessCardHandler::generateQrCode);
    }
}
