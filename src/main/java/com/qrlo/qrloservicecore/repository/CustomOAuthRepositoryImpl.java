package com.qrlo.qrloservicecore.repository;

import com.qrlo.qrloservicecore.model.OAuth;
import com.qrlo.qrloservicecore.model.OAuthType;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-18
 */
@Component
public class CustomOAuthRepositoryImpl implements CustomOAuthRepository {
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public CustomOAuthRepositoryImpl(R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    @Override
    public Mono<OAuth> findByOAuthTypeAndConnectionId(OAuthType oAuthType, String connectionId) {
        return r2dbcEntityTemplate
                .select(OAuth.class)
                .matching(Query.query(Criteria.where("oauth_type").is(oAuthType.getValue()).and("connection_id").is(connectionId)))
                .one();
    }
}
