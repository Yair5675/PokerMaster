package com.example.pokermaster.hands.creators.exceptions;

public class PokerHandCreatorException extends Exception {
  public PokerHandCreatorException() {
  }

  public PokerHandCreatorException(String message) {
    super(message);
  }

  public PokerHandCreatorException(String message, Throwable cause) {
    super(message, cause);
  }

  public PokerHandCreatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public PokerHandCreatorException(Throwable cause) {
    super(cause);
  }
}
