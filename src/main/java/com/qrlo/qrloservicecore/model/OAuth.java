package com.qrlo.qrloservicecore.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-21
 */
@Data
@AllArgsConstructor
public class OAuth {
    private OAuthType oAuthType;
    private String connectionId;
}
