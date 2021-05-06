package com.qrlo.qrloservicecore.user.store;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-30
 */
@Configuration
public class StoreRouter {
    @Bean
    public RouterFunction<ServerResponse> storeRoutes(StoreHandler storeHandler) {
        return RouterFunctions
                .route(RequestPredicates.GET("store"), storeHandler::fetchStore);
    }
}
