package com.password.creator.passwordgenerator.models.sources;

/**
 * Created by nttungg on 6/19/18.
 */

public class CryptoException extends Exception{
    public CryptoException() {
    }

    public CryptoException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
