package com.qrlo.qrloservicecore.user.service;

import com.qrlo.qrloservicecore.user.model.OAuth;
import com.qrlo.qrloservicecore.user.model.Role;
import com.qrlo.qrloservicecore.user.model.User;
import com.qrlo.qrloservicecore.user.repository.UserRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-22
 */
@Service
public class UserService {
    private static final String MOCK_USER_EMAIL = "test@example.com";
    private final UserRepository userRepository;
    private final ReactiveMongoTemplate mongoTemplate;

    public UserService(UserRepository userRepository, ReactiveMongoTemplate mongoTemplate) {
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public Mono<User> findById(String id) {
        return userRepository.findById(id);
    }

    public Mono<User> findByOAuth(OAuth oAuth) {
        return userRepository.findByOAuth(oAuth);
    }

    public Mono<User> findByOAuthOrInsert(OAuth oAuth) {
        return userRepository.findByOAuth(oAuth).switchIfEmpty(Mono.defer(() -> {
            User newUser = User.builder()
                    .email(MOCK_USER_EMAIL)
                    .roles(List.of(Role.ROLE_USER))
                    .oAuths(List.of(oAuth))
                    .build();
            return userRepository.save(newUser);
        }));
    }
}
