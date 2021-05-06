package com.qrlo.qrloservicecore.user.client.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-29
 */
public class OAuthVerificationException extends ResponseStatusException {
    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public OAuthVerificationException() {
        super(HttpStatus.UNAUTHORIZED, "Failed to verify OAuth with partner");
    }
}
