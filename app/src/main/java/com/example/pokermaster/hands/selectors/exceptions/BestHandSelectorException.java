package com.example.pokermaster.hands.selectors.exceptions;

public class BestHandSelectorException extends Exception { // TODO: Create top-level PokerMasterException

    public BestHandSelectorException() {
    }

    public BestHandSelectorException(String message) {
        super(message);
    }

    public BestHandSelectorException(String message, Throwable cause) {
        super(message, cause);
    }

    public BestHandSelectorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BestHandSelectorException(Throwable cause) {
        super(cause);
    }
}
