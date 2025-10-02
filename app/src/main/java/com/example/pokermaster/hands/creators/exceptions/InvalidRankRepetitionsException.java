package com.example.pokermaster.hands.creators.exceptions;

public class InvalidRankRepetitionsException extends PokerHandCreatorException {
    public InvalidRankRepetitionsException() {
    }

    public InvalidRankRepetitionsException(String message) {
        super(message);
    }

    public InvalidRankRepetitionsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRankRepetitionsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InvalidRankRepetitionsException(Throwable cause) {
        super(cause);
    }
}
