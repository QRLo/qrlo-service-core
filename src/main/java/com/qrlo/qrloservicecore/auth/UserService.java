package com.qrlo.qrloservicecore.auth;

import com.qrlo.qrloservicecore.auth.model.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-22
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Mono<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
