package com.qrlo.qrloservicecore.service.exception;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-27
 */
public class InvalidVCardException extends RuntimeException {
    public InvalidVCardException(String msg) {
        super(msg);
    }

    public InvalidVCardException(String msg, Throwable e) {
        super(msg, e);
    }
}
