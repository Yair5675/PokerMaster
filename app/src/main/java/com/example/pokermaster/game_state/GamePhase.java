package com.example.pokermaster.game_state;

/**
 * The phase of a game of poker, dictates which actions can be taken, how much players can
 * bet/raise, and the number of cards revealed.
 */
public enum GamePhase {
    /*
     * First phase - after seeing their hole cards, each player can fold, call or raise the big
     * blind
     */
    PRE_FLOP,

    /*
     * Three community cards are dealt, players can again fold, call/check (depending on previous
     *  bets) or raise.
     */
    FLOP,

    /*
     * Fourth community card was dealt,.
     */
    TURN,

    /*
     * Final community card is dealt (the river).
     */
    RIVER,

    /*
     * All players reveal their cards.
     */
    SHOWDOWN
}
