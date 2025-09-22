package com.example.pokermaster.game_state;

/**
 * Data class representing the two private cards dealt to each player at the start of the game.
 */
public class HoleCards {
    private Card mFirstCard;
    private Card mSecondCard;

    public HoleCards(Card firstCard, Card secondCard) {
        mFirstCard = firstCard;
        mSecondCard = secondCard;
    }

    public Card getFirstCard() {
        return mFirstCard;
    }

    public Card getSecondCard() {
        return mSecondCard;
    }

    public void setFirstCard(Card firstCard) {
        mFirstCard = firstCard;
    }

    public void setSecondCard(Card secondCard) {
        mSecondCard = secondCard;
    }
}
