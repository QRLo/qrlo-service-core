package com.qrlo.qrloservicecore.model;

import lombok.Getter;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-22
 */

public enum Role {
    ROLE_USER("USER"), ROLE_ADMIN("ADMIN");

    @Getter
    private final String value;

    Role(String value) {
        this.value = value;
    }
}
