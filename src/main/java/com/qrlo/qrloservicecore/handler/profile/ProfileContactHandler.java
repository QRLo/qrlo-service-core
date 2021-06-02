package com.qrlo.qrloservicecore.handler.profile;

import com.qrlo.qrloservicecore.handler.profile.domain.AddContactRequest;
import com.qrlo.qrloservicecore.service.ContactService;
import com.qrlo.qrloservicecore.util.RequestUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-29
 */
@Component
public class ProfileContactHandler {
    private final ContactService contactService;

    public ProfileContactHandler(ContactService contactService) {
        this.contactService = contactService;
    }

    public Mono<ServerResponse> getAllContacts(ServerRequest request) {
        return RequestUtils
                .getUserIdFromRequestPrincipal(request)
                .flatMapMany(contactService::fetchAllContacts)
                .collectList()
                .flatMap(businessCards -> ServerResponse.ok().bodyValue(businessCards));
    }

    public Mono<ServerResponse> addContact(ServerRequest request) {
        Mono<Integer> userIdMono = RequestUtils.getUserIdFromRequestPrincipal(request);
        return request
                .bodyToMono(AddContactRequest.class)
                .zipWith(userIdMono)
                .flatMap(t -> contactService.addContact(t.getT2(), t.getT1().getBusinessCardId()))
                .flatMap(userBusinessCard -> ServerResponse.ok().bodyValue(userBusinessCard));
    }
}
