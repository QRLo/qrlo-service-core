package com.qrlo.qrloservicecore.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.qrlo.qrloservicecore.model.Role;
import com.qrlo.qrloservicecore.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-21
 */
@Component
public final class JwtTokenProvider {
    private final String keyStoreFilePath;
    private final String keyStorePassword;
    private final String keyAlias;
    private final String privateKeyPassword;
    private final String duration;

    private PrivateKey privateKey;
    private X509Certificate certificate;
    private Algorithm signingAlgorithm;

    public JwtTokenProvider(
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
        signingAlgorithm = Algorithm.ECDSA256((ECPublicKey) certificate.getPublicKey(), (ECPrivateKey) privateKey);
    }

    public DecodedJWT verifyJwtToken(String token) {
        return JWT.require(signingAlgorithm).build().verify(token);
    }

    public String generateToken(User user) {
        final Instant now = Instant.now();
        return JWT.create()
                .withIssuer("qrlo-service-core")
                .withJWTId(UUID.randomUUID().toString())
                .withSubject(user.getId())
                .withIssuedAt(Date.from(now))
                .withNotBefore(Date.from(now))
                .withExpiresAt(Date.from(now.plus(Duration.parse(duration))))
                .withClaim("roles", user.getRoles().stream().map(Role::getValue).collect(Collectors.toList()))
                .sign(signingAlgorithm);
    }

    public Mono<String> generateTokenMono(User user) {
        return Mono.just(generateToken(user));
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = verifyJwtToken(token);
        User user = User.builder().id(decodedJWT.getSubject()).build();
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(decodedJWT.getClaim("roles").toString());
        return new UsernamePasswordAuthenticationToken(user, token, authorities);
    }
}
