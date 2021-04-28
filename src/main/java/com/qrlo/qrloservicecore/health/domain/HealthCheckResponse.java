package com.qrlo.qrloservicecore.health.domain;

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
}
