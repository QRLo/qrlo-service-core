package com.qrlo.qrloservicecore.client.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-29
 */
public class OAuthVerificationException extends ResponseStatusException {
    public OAuthVerificationException() {
        super(HttpStatus.UNAUTHORIZED, "Failed to verify with OAuth Partner");
    }
}
