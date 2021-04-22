package com.qrlo.qrloservicecore.common.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-21
 */
@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {
    private final AuthenticationManager authenticationManager;

    public SecurityContextRepository(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Saves the SecurityContext
     *
     * @param exchange the exchange to associate to the SecurityContext
     * @param context  the SecurityContext to save
     * @return a completion notification (success or error)
     */
    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return null;
    }

    /**
     * Loads the SecurityContext associated with the {@link ServerWebExchange}
     *
     * @param exchange the exchange to look up the {@link SecurityContext}
     * @return the {@link SecurityContext} to lookup or empty if not found. Never null
     */
    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        final String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.empty();
        }
        final String authToken = authHeader.replaceFirst("Bearer ", "");

        final Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
        return this.authenticationManager.authenticate(auth).map((SecurityContextImpl::new));
    }
}
