package com.qrlo.qrloservicecore.user.store;

import com.qrlo.qrloservicecore.user.model.User;
import com.qrlo.qrloservicecore.user.store.model.Store;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-30
 */
@Component
public class StoreHandler {
    private final StoreRepository storeRepository;
    public StoreHandler(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public Mono<ServerResponse> fetchStore(ServerRequest request) {
        return request
                .principal()
                .ofType(UsernamePasswordAuthenticationToken.class)
                .map(UsernamePasswordAuthenticationToken::getPrincipal)
                .ofType(User.class)
                .map(User::getId)
                .flatMap(storeRepository::findByUserId)
                .flatMap(store -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Mono.just(store), Store.class));
    }
}
