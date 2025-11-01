package com.example.pokermaster.cards.exceptions;

public class EmptyDeckException extends Exception {
    public EmptyDeckException() {
    }

    public EmptyDeckException(String message) {
        super(message);
    }

    public EmptyDeckException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyDeckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EmptyDeckException(Throwable cause) {
        super(cause);
    }
}
