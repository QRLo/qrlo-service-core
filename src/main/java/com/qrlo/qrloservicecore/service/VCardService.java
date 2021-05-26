package com.qrlo.qrloservicecore.service;

import com.qrlo.qrloservicecore.model.UserBusinessCard;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.parameter.EmailType;
import ezvcard.property.StructuredName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-10
 */
@Service
public class VCardService {
    @Value("${qrlo.vcard.businesscard.id.property-name}")
    private String businessCardIdPropertyName;
    @Value("${qrlo.vcard.businesscard.userId.property-name}")
    private String businessCardUserIdPropertyName;


    public Mono<VCard> generateFromUserBusinessCard(UserBusinessCard userBusinessCard) {
        VCard vCard = new VCard();
        StructuredName name = new StructuredName();
        name.setFamily(userBusinessCard.getLastName());
        name.setGiven(userBusinessCard.getFirstName());
        vCard.setStructuredName(name);
        vCard.setOrganization(userBusinessCard.getCompany());
        vCard.addTitle(userBusinessCard.getPosition());
        vCard.addEmail(userBusinessCard.getEmail(), EmailType.WORK);
        vCard.addTelephoneNumber(userBusinessCard.getPhone());
        vCard.setExtendedProperty(businessCardIdPropertyName, userBusinessCard.getId().toString());
        vCard.setExtendedProperty(businessCardUserIdPropertyName, userBusinessCard.getUserId().toString());
        return Mono.just(vCard);
    }

    public Mono<String> writeVCardAsString(VCard vCard) {
        return Mono.fromCallable(() -> Ezvcard.write(vCard).version(VCardVersion.V3_0).go());
    }
}
