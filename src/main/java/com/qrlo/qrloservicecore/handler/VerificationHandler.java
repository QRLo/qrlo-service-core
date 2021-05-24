package com.qrlo.qrloservicecore.handler;

import com.qrlo.qrloservicecore.config.security.JwtTokenProvider;
import com.qrlo.qrloservicecore.service.BusinessCardService;
import com.qrlo.qrloservicecore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-24
 */
@Slf4j
@Component
public class VerificationHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final BusinessCardService businessCardService;

    public VerificationHandler(JwtTokenProvider jwtTokenProvider, UserService userService, BusinessCardService businessCardService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.businessCardService = businessCardService;
    }

    public Mono<ServerResponse> verifyAccountActivationToken(ServerRequest request) {
        log.info("WTFF???");
        return jwtTokenProvider.getSubjectFromToken(request.pathVariable("token"))
                .flatMap(userService::findById)
                .doOnNext(user -> user.setVerified(true))
                .flatMap(userService::saveUser)
                .then(ServerResponse.ok().contentType(MediaType.TEXT_HTML).render("verification/checked"));
    }

    public Mono<ServerResponse> verifyBusinessCardActivationToken(ServerRequest request) {
        return jwtTokenProvider.getSubjectFromToken(request.pathVariable("token"))
                .flatMap(businessCardService::findById)
                .doOnNext(businessCard -> businessCard.setEmailVerified(true))
                .flatMap(businessCardService::saveBusinessCard)
                .then(ServerResponse.ok().contentType(MediaType.TEXT_HTML).render("verification/checked"));
    }
}
