package com.back.vd.domain.exception;

public class MediaStorageException extends DomainException {
    public MediaStorageException(String message) {
        super(message);
    }

    public MediaStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
