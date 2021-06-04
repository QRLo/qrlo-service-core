package com.qrlo.qrloservicecore.repository;

import com.qrlo.qrloservicecore.model.Contact;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-29
 */
public interface ContactRepository extends R2dbcRepository<Contact, Integer> {
}
