package com.qrlo.qrloservicecore.repository;

import com.qrlo.qrloservicecore.model.OAuth;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-17
 */
@Repository
public interface OAuthRepository extends R2dbcRepository<OAuth, Integer>, CustomOAuthRepository {
}
