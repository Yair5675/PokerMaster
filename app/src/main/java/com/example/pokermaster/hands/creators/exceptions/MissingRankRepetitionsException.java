package com.example.pokermaster.hands.creators.exceptions;

public class MissingRankRepetitionsException extends PokerHandCreatorException {
  public MissingRankRepetitionsException() {
  }

  public MissingRankRepetitionsException(String message) {
    super(message);
  }

  public MissingRankRepetitionsException(String message, Throwable cause) {
    super(message, cause);
  }

  public MissingRankRepetitionsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public MissingRankRepetitionsException(Throwable cause) {
    super(cause);
  }
}
