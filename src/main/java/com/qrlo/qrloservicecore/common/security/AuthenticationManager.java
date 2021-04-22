package com.qrlo.qrloservicecore.common.security;

import com.qrlo.qrloservicecore.auth.UserService;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-21
 */
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthenticationManager(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    /**
     * Attempts to authenticate the provided {@link Authentication}
     *
     * @param authentication the {@link Authentication} to test
     * @return if authentication is successful an {@link Authentication} is returned. If
     * authentication cannot be determined, an empty Mono is returned. If authentication
     * fails, a Mono error is returned.
     */
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        try {
            Long userId = jwtUtil.getUserIdFromToken(authToken);
            return userService.findById(userId).map(user -> new UsernamePasswordAuthenticationToken(user, null));
        } catch (JwtException e) {
            return Mono.empty();
        }
    }
}
