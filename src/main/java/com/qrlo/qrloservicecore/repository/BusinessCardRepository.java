package com.qrlo.qrloservicecore.repository;

import com.qrlo.qrloservicecore.model.BusinessCard;
import com.qrlo.qrloservicecore.model.UserBusinessCard;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-10
 */
@Repository
public interface BusinessCardRepository extends R2dbcRepository<BusinessCard, Integer> {
    Flux<BusinessCard> findBusinessCardByUserId(Integer userId);

    @Query("SELECT * FROM user_business_cards WHERE id = :id")
    Mono<UserBusinessCard> findOneBusinessCardById(@Param("id") Integer id);
}
