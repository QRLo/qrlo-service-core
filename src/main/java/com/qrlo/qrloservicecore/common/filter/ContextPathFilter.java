package com.qrlo.qrloservicecore.common.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-22
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ContextPathFilter implements WebFilter {
    private final String contextPath;

    public ContextPathFilter(@Value("${server.path.global-prefix}") String globalPrefix, @Value("${server.api.version}") String apiVersion) {
        this.contextPath = String.format("%s/%s", globalPrefix, apiVersion);
    }

    /**
     * Process the Web request and (optionally) delegate to the next
     * {@code WebFilter} through the given {@link WebFilterChain}.
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     * @return {@code Mono<Void>} to indicate when request processing is complete
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String requestPath = request.getURI().getPath();
        if (requestPath.startsWith(contextPath + "/") || requestPath.equals(contextPath)) {
            return
                    chain.filter(
                            exchange.mutate()
                                    .request(request.mutate().contextPath(contextPath).build())
                                    .build()
                    );
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
