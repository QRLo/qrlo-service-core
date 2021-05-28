package com.qrlo.qrloservicecore.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qrlo.qrloservicecore.model.UserBusinessCard;
import com.qrlo.qrloservicecore.service.domain.BusinessCardVCardData;
import com.qrlo.qrloservicecore.service.exception.InvalidVCardException;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.parameter.EmailType;
import ezvcard.property.StructuredName;
import org.bouncycastle.util.encoders.Base64;
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
    @Value("${qrlo.vcard.businesscard.property-name}")
    private String businessCardPropertyName;

    private final ObjectMapper objectMapper = new ObjectMapper();

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
            try {
                BusinessCardVCardData businessCardVCardData = new BusinessCardVCardData(userBusinessCard.getId(), userBusinessCard.getUserId());
                String encodedBusinessCardData = Base64.toBase64String(objectMapper.writeValueAsBytes(businessCardVCardData));
                vCard.setExtendedProperty(businessCardPropertyName, encodedBusinessCardData);
            } catch (JsonProcessingException e) {
                String msg = String.format("Failed to write business card for user: %d, businesscard %d", userBusinessCard.getUserId(), userBusinessCard.getId());
                throw new InvalidVCardException(msg, e);
            }
            return vCard;
        });
    }

    public Mono<String> writeVCardAsString(VCard vCard) {
        return Mono.fromCallable(() -> Ezvcard.write(vCard).version(VCardVersion.V3_0).go());
    }
}
