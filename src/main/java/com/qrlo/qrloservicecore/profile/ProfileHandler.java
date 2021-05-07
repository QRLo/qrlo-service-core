package com.qrlo.qrloservicecore.profile;

import com.qrlo.qrloservicecore.user.UserService;
import com.qrlo.qrloservicecore.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-22
 */
@Slf4j
@Component
public class ProfileHandler {
    private final UserService userService;

    public ProfileHandler(UserService userService) {
        this.userService = userService;
    }

    public Mono<ServerResponse> getProfile(ServerRequest request) {
        return request
                .principal()
                .ofType(UsernamePasswordAuthenticationToken.class)
                .map(UsernamePasswordAuthenticationToken::getPrincipal)
                .ofType(User.class)
                .map(User::getId)
                .flatMap(userService::findById)
                .flatMap(user -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Mono.just(user), User.class));
    }
}
