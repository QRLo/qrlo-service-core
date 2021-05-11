package com.qrlo.qrloservicecore.repository;

import com.qrlo.qrloservicecore.model.UnwoundUserBusinessCard;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-10
 */
@Repository
public interface BusinessCardRepository {
    Mono<UnwoundUserBusinessCard> findUnwoundBusinessCardForUserById(String userId, String businessCardId);
}
