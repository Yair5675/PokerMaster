package com.example.pokermaster.cards.exceptions;

public class CombinationIndexOverflowException extends RuntimeException {
    public CombinationIndexOverflowException() {
    }

    public CombinationIndexOverflowException(String message) {
        super(message);
    }

    public CombinationIndexOverflowException(String message, Throwable cause) {
        super(message, cause);
    }

    public CombinationIndexOverflowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CombinationIndexOverflowException(Throwable cause) {
        super(cause);
    }
}
