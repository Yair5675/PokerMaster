package com.example.pokermaster.hands.creators.exceptions;

public class InvalidHandSizeException extends PokerHandCreatorException {
    public InvalidHandSizeException() {
    }

    public InvalidHandSizeException(String message) {
        super(message);
    }

    public InvalidHandSizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidHandSizeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InvalidHandSizeException(Throwable cause) {
        super(cause);
    }
}
