package com.qrlo.qrloservicecore.repository;

import com.qrlo.qrloservicecore.model.BusinessCard;
import com.qrlo.qrloservicecore.model.UserBusinessCard;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-10
 */
@Repository
public interface BusinessCardRepository {
    Mono<UserBusinessCard> findUnwoundBusinessCardForUserById(String userId, String businessCardId);

    Mono<BusinessCard> verifyBusinessCardById(String businessCardId);
}
