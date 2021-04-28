package com.qrlo.qrloservicecore.user.repository;

import com.qrlo.qrloservicecore.user.model.OAuth;
import com.qrlo.qrloservicecore.user.model.User;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-22
 */
@Component
public class ComplexUserRepositoryImpl implements ComplexUserRepository {
    private final ReactiveMongoTemplate template;

    public ComplexUserRepositoryImpl(ReactiveMongoTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<User> findByOAuth(OAuth oAuth) {
        return template.findOne(Query.query(Criteria.where("oAuths").in(oAuth)), User.class);
    }
}
