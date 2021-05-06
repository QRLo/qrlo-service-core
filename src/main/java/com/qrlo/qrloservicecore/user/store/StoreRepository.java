package com.qrlo.qrloservicecore.user.store;

import com.qrlo.qrloservicecore.user.store.model.Store;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-30
 */
@Repository
public interface StoreRepository extends ReactiveMongoRepository<Store, String> {
    Mono<Store> findByUserId(String userId);
}
