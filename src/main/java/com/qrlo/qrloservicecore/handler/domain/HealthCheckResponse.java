package com.qrlo.qrloservicecore.handler.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-22
 */
@Getter
@AllArgsConstructor
@JsonInclude
public class HealthCheckResponse {
    private final HealthStatus status;
    private final boolean isLoggedIn;
}
