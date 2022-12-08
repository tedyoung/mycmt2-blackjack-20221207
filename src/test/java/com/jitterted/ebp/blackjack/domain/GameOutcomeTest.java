package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameOutcomeTest {

    @Test
    void playerHitsAndGoesBustThenOutcomeIsPlayerLoses() {
        Game game = new Game(StubDeck.playerHitsAndGoesBust());
        game.initialDeal();

        game.playerHits();

        assertThat(game.determineOutcome())
                .isEqualByComparingTo(GameOutcome.PLAYER_BUSTED);
    }

    @Test
    void playerDealtBetterHandThanDealerAndStandsThenPlayerBeatsDealer() {
        Game game = new Game(StubDeck.playerStandsAndBeatsDealer());
        game.initialDeal();

        game.playerStands();
        game.dealerTurn();

        assertThat(game.determineOutcome())
                .isEqualByComparingTo(GameOutcome.PLAYER_BEATS_DEALER);
    }

    @Test
    void playerDealtBlackjackUponInitialDealThenWinsBlackjack() {
        Deck deck = new StubDeck(Rank.TEN, Rank.EIGHT,
                                 Rank.ACE, Rank.JACK);
        Game game = new Game(deck);

        game.initialDeal();

        assertThat(game.determineOutcome())
                .isEqualByComparingTo(GameOutcome.PLAYER_WINS_BLACKJACK);
    }
}