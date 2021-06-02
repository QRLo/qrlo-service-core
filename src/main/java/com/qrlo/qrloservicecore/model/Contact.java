package com.qrlo.qrloservicecore.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-29
 */
@Data
@Builder
@Table("contacts")
public class Contact {
    @Id
    private int id;
    @Column("business_card_id")
    private int businessCardId;
    @Column("user_id")
    private int userId;
    @Column("created_at")
    @CreatedDate
    private LocalDateTime createdAt;
    @Column("updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @Column("version")
    private long version;
}
