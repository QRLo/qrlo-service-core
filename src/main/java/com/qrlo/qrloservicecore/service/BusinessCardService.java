package com.qrlo.qrloservicecore.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qrlo.qrloservicecore.model.BusinessCard;
import com.qrlo.qrloservicecore.model.UnwoundUserBusinessCard;
import com.qrlo.qrloservicecore.repository.BusinessCardRepository;
import com.qrlo.qrloservicecore.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-06
 */
@Service
public class BusinessCardService {
    private final UserRepository userRepository;
    private final BusinessCardRepository businessCardRepository;

    public BusinessCardService(UserRepository userRepository, BusinessCardRepository businessCardRepository) {
        this.userRepository = userRepository;
        this.businessCardRepository = businessCardRepository;
    }

    public Mono<BusinessCard> saveBusinessCardForUser(BusinessCard businessCard, String userId) {
        businessCard.setId(new ObjectId().toString());
        return userRepository
                .findById(userId)
                .doOnNext(user -> user.getMyBusinessCards().add(businessCard))
                .flatMap(userRepository::save)
                .thenReturn(businessCard);
    }

    public Mono<UnwoundUserBusinessCard> getUnwoundBusinessCardForUserById(String businessCardId, String userId) {
        return businessCardRepository.findUnwoundBusinessCardForUserById(userId, businessCardId);
    }
}
