package com.qrlo.qrloservicecore.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-21
 */
@Table("oauths")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuth {
    @Id
    private Integer id;
    @Column("user_id")
    private Integer userId;
    @Column("oauth_type")
    private String oAuthType;
    @Column("connection_id")
    private String connectionId;
    @Column("version")
    @Version
    private Long version;

    public OAuth(String oAuthType, String connectionId) {
        this.oAuthType = oAuthType;
        this.connectionId = connectionId;
    }
}
