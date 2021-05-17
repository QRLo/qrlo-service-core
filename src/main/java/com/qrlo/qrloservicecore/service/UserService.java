package com.qrlo.qrloservicecore.service;

import com.qrlo.qrloservicecore.model.BusinessCard;
import com.qrlo.qrloservicecore.model.OAuth;
import com.qrlo.qrloservicecore.model.User;
import com.qrlo.qrloservicecore.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-22
 */
@Service
public class UserService implements ReactiveUserDetailsService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> findById(String id) {
        return userRepository.findById(id);
    }

    public Mono<BusinessCard> addMyBusinessCard(String id, BusinessCard businessCard) {
        businessCard.setId(new ObjectId().toString());
        businessCard.setEmailVerified(false);
        return userRepository
                .findById(id)
                .doOnNext(user -> user.getMyBusinessCards().add(businessCard))
                .flatMap(userRepository::save)
                .thenReturn(businessCard);
    }

    public Mono<User> findByOAuth(OAuth oAuth) {
        return userRepository.findByOAuth(oAuth);
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
