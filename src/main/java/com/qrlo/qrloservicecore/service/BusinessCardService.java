package com.qrlo.qrloservicecore.service;

import com.qrlo.qrloservicecore.model.BusinessCard;
import com.qrlo.qrloservicecore.model.UserBusinessCard;
import com.qrlo.qrloservicecore.repository.BusinessCardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-06
 */
@Slf4j
@Service
public class BusinessCardService {
    private final BusinessCardRepository businessCardRepository;

    public BusinessCardService(BusinessCardRepository businessCardRepository) {
        this.businessCardRepository = businessCardRepository;
    }

    public Mono<BusinessCard> saveBusinessCardForUser(BusinessCard businessCard, Integer userId) {
        businessCard.setUserId(userId);
        return businessCardRepository.save(businessCard);
    }

    public Flux<BusinessCard> getAllBusinessCardsByUserId(Integer userId) {
        return businessCardRepository.findBusinessCardByUserId(userId);
    }

    public Mono<UserBusinessCard> getBusinessCardForUserById(Integer id, Integer userId) {
        return businessCardRepository.findOneBusinessCardByIdAAndUserId(id, userId);
    }
}
