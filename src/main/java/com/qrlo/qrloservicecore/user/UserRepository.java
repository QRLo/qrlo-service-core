package com.qrlo.qrloservicecore.user;

import com.qrlo.qrloservicecore.user.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-21
 */
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String>, ComplexUserRepository {
    Mono<User> findByEmail(String email);
}
