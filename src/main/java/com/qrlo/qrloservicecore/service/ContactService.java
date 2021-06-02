package com.qrlo.qrloservicecore.service;

import com.qrlo.qrloservicecore.model.BusinessCard;
import com.qrlo.qrloservicecore.model.Contact;
import com.qrlo.qrloservicecore.model.UserBusinessCard;
import com.qrlo.qrloservicecore.repository.BusinessCardRepository;
import com.qrlo.qrloservicecore.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-29
 */
@RequiredArgsConstructor
@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final BusinessCardRepository businessCardRepository;

    public Flux<BusinessCard> fetchAllContacts(int userId) {
        return businessCardRepository
                .findBusinessCardByUserId(userId);
    }

    @Transactional
    public Mono<UserBusinessCard> addContact(int userId, int businessCardId) {
        Contact newContact = Contact.builder()
                .businessCardId(businessCardId)
                .userId(userId)
                .build();
        return contactRepository.save(newContact)
                .map(Contact::getBusinessCardId)
                .flatMap(businessCardRepository::findOneBusinessCardById);
    }
}
