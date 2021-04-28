package com.qrlo.qrloservicecore.user.repository;

import com.qrlo.qrloservicecore.user.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-21
 */
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String>, ComplexUserRepository {
}
