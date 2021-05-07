package com.qrlo.qrloservicecore.user;

import com.qrlo.qrloservicecore.auth.domain.OAuthIntegrationRequest;
import com.qrlo.qrloservicecore.user.model.OAuth;
import com.qrlo.qrloservicecore.user.model.Role;
import com.qrlo.qrloservicecore.user.model.User;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-22
 */
@Service
public class UserService implements ReactiveUserDetailsService {
    private static final String MOCK_USER_EMAIL = "test@example.com";
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> findById(String id) {
        return userRepository.findById(id);
    }

    public Mono<User> findByOAuth(OAuth oAuth) {
        return userRepository.findByOAuth(oAuth);
    }

    public Mono<User> createUser() {
        return null;
    }

    public Mono<User> createUser(OAuth oAuth, OAuthIntegrationRequest oAuthIntegrationRequest) {
        return Mono.just(User.builder().email(oAuthIntegrationRequest.getEmail()).oAuths(List.of(oAuth)).roles(List.of(Role.ROLE_USER)).build())
                .flatMap(userRepository::save);
    }

    public Mono<User> saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Find the {@link UserDetails} by username.
     *
     * @param username the username to look up
     * @return the {@link UserDetails}. Cannot be null
     */
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByEmail(username).cast(UserDetails.class);
    }
}
