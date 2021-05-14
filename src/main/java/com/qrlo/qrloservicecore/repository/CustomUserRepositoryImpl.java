package com.qrlo.qrloservicecore.repository;

import com.qrlo.qrloservicecore.model.OAuth;
import com.qrlo.qrloservicecore.model.User;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-22
 */
@Component
public class CustomUserRepositoryImpl implements CustomUserRepository {
    private final ReactiveMongoTemplate template;

    public CustomUserRepositoryImpl(ReactiveMongoTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<User> findByOAuth(OAuth oAuth) {
        return template.findOne(Query.query(Criteria.where("oAuths").in(oAuth)), User.class);
    }


}
