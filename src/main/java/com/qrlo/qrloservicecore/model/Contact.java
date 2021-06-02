package com.qrlo.qrloservicecore.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-29
 */
@Table("contacts")
public class Contact {
    @Id
    private int id;
    @Column("business_card_id")
    private int businessCardId;
    @Column("user_id")
    private int userId;
    @Column("version")
    private long version;
}
