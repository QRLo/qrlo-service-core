package com.qrlo.qrloservicecore.service;

import lombok.NonNull;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-14
 */
@Service
public class OTPService {
    private static final String CACHE_NAME = "OTP";
    private final CacheManager cacheManager;

    public OTPService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public Mono<String> createOTP(String email) {
        final String otp = new DecimalFormat("000000").format(new SecureRandom().nextInt(999999));
        Objects.requireNonNull(cacheManager.getCache(CACHE_NAME)).put(email, otp);
        return Mono.just(otp);
    }

    public Mono<String> fetchOTP(String email) {
        return Mono.justOrEmpty(Objects.requireNonNull(cacheManager.getCache(CACHE_NAME)).get(email, String.class));
    }
}
