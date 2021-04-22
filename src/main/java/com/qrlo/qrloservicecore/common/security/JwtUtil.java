package com.qrlo.qrloservicecore.common.security;

import com.qrlo.qrloservicecore.auth.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-21
 */
@Component
public final class JwtUtil {
    private final String keyStoreFilePath;
    private final String keyStorePassword;
    private final String keyAlias;
    private final String privateKeyPassword;
    private final String duration;

    private PrivateKey privateKey;
    private X509Certificate certificate;

    public JwtUtil(
            @Value("${qrlo.security.keystore.file}") String keyStoreFilePath,
            @Value("${qrlo.security.keystore.password}") String keyStorePassword,
            @Value("${qrlo.security.jwt.alias}") String keyAlias,
            @Value("${qrlo.security.jwt.sk.password}") String privateKeyPassword,
            @Value("${qrlo.security.jwt.duration}") String duration) {
        this.keyStoreFilePath = keyStoreFilePath;
        this.keyStorePassword = keyStorePassword;
        this.keyAlias = keyAlias;
        this.privateKeyPassword = privateKeyPassword;
        this.duration = duration;
    }

    @PostConstruct
    public void init() throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        KeyStore keyStore = KeyStore.getInstance(new ClassPathResource(keyStoreFilePath).getFile(), keyStorePassword.toCharArray());
        certificate = (X509Certificate) keyStore.getCertificate(keyAlias);
        privateKey = (PrivateKey) keyStore.getKey(keyAlias, privateKeyPassword.toCharArray());
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(certificate.getPublicKey()).build().parseClaimsJws(token).getBody();
    }

    public Long getUserIdFromToken(String token) {
        return Long.valueOf(getAllClaimsFromToken(token).getSubject());
    }

    public String generateToken(User user) {
        Map<String, Object> claims = Map.of(
                "roles", user.getRoles()
        );
        final Instant now = Instant.now();
        return Jwts.builder()
                .setIssuer("qrlo-service-core")
                .setSubject(user.getId().toString())
                .setIssuedAt(Date.from(now))
                .setNotBefore(Date.from(now))
                .setExpiration(Date.from(now.plus(Duration.parse(duration))))
                .addClaims(claims)
                .signWith(privateKey)
                .compact();
    }
}
