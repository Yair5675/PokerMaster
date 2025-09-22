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

    /**
     * Checks if both cards have the same {@link Suit}.
     * @return True if both cards have the same suit.
     */
    public boolean isSuited() {
        return mFirstCard.getSuit() == mSecondCard.getSuit();
    }

    /**
     * Checks if both cards do NOT have the same {@link Suit}.
     * @return True if the suits of the cards differ.
     */
    public boolean isOffSuited() {
        return mFirstCard.getSuit() != mSecondCard.getSuit();
    }
}
