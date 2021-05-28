package com.qrlo.qrloservicecore.service.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-27
 */
@Data
@AllArgsConstructor
public class BusinessCardVCardData {
    @JsonProperty("id")
    private int id;
    @JsonProperty("userId")
    private int userId;
}
