package com.qrlo.qrloservicecore.service;

import com.qrlo.qrloservicecore.model.UserBusinessCard;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.parameter.EmailType;
import ezvcard.property.StructuredName;
import ezvcard.property.Uid;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-10
 */
@Service
public class VCardService {
    public Mono<VCard> generateFromUserBusinessCard(UserBusinessCard userBusinessCard) {
        return Mono.fromCallable(() -> {
            VCard vCard = new VCard();
            StructuredName name = new StructuredName();
            name.setFamily(userBusinessCard.getLastName());
            name.setGiven(userBusinessCard.getFirstName());
            vCard.setStructuredName(name);
            vCard.setOrganization(userBusinessCard.getCompany());
            vCard.addTitle(userBusinessCard.getPosition());
            vCard.addEmail(userBusinessCard.getEmail(), EmailType.WORK);
            vCard.addTelephoneNumber(userBusinessCard.getPhone());
            vCard.setUid(new Uid(userBusinessCard.getId().toString()));
            return vCard;
        });
    }

    public Mono<String> writeVCardAsString(VCard vCard) {
        return Mono.fromCallable(() -> Ezvcard.write(vCard).version(VCardVersion.V3_0).go());
    }
}
