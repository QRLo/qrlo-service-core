package com.qrlo.qrloservicecore.user.client.exception;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-29
 */
public class OAuthVerificationException extends RuntimeException {
    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public OAuthVerificationException() {
        super("Failed to verify OAuth with partner");
    }
}
