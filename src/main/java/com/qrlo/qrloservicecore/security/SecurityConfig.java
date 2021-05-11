package com.qrlo.qrloservicecore.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-21
 */
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public ReactiveAuthenticationManager authenticationManager(ReactiveUserDetailsService detailsService) {
        return new UserDetailsRepositoryReactiveAuthenticationManager(detailsService);
    }

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http, JwtTokenProvider tokenProvider) {
        return http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
                .accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))).and()
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange()
                    .pathMatchers(HttpMethod.OPTIONS).permitAll()
                    .pathMatchers("/auth/**").permitAll()
                    .pathMatchers("/health").permitAll()
                .pathMatchers("/businesscards/**").permitAll()
                    .anyExchange().authenticated().and()
                .addFilterAt(new JwtAuthenticationFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
                .build();
    }
}
