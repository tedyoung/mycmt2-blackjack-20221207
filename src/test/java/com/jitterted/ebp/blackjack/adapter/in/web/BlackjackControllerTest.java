package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Card;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import com.jitterted.ebp.blackjack.domain.Suit;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class BlackjackControllerTest {

    @Test
    void startGameThenInitialDealPlayerNotDealtBlackjack() throws Exception {
        Game game = new Game(StubDeck.playerNotDealtBlackjack());
        BlackjackController blackjackController = new BlackjackController(game);

        String redirectString = blackjackController.startGame();

        assertThat(game.playerHand().cards())
                .hasSize(2);
        assertThat(redirectString)
                .isEqualTo("redirect:/game");
        assertThat(game.isPlayerDone())
                .isFalse();
    }

    @Test
    void startGameThenInitialDealPlayerDealtBlackjack() throws Exception {
        Game game = new Game(StubDeck.playerDealtBlackjack());
        BlackjackController blackjackController = new BlackjackController(game);

        String redirectString = blackjackController.startGame();

        assertThat(game.isPlayerDone())
                .isTrue();
        assertThat(redirectString)
                .isEqualTo("redirect:/done");
    }

    @Test
    public void gameViewPopulatesViewModelWithAllCards() throws Exception {
        Deck stubDeck = new StubDeck(List.of(new Card(Suit.DIAMONDS, Rank.TEN),
                                             new Card(Suit.HEARTS, Rank.TWO),
                                             new Card(Suit.DIAMONDS, Rank.KING),
                                             new Card(Suit.CLUBS, Rank.THREE)));
        Game game = new Game(stubDeck);
        BlackjackController blackjackController = new BlackjackController(game);
        blackjackController.startGame();
        Model model = new ConcurrentModel();

        blackjackController.gameView(model);

        GameView gameView = (GameView) model.getAttribute("gameView");

        assertThat(gameView.getDealerCards())
                .containsExactly("2???", "3???");

        assertThat(gameView.getPlayerCards())
                .containsExactly("10???", "K???");
    }

    @Test
    public void hitCommandResultsInThirdCardDealtToPlayer() throws Exception {
        Game game = new Game(StubDeck.playerHitsDoesNotBust());
        BlackjackController blackjackController = new BlackjackController(game);
        blackjackController.startGame();

        String redirectPage = blackjackController.hitCommand();

        assertThat(redirectPage)
                .isEqualTo("redirect:/game");
        assertThat(game.playerHand().cards())
                .hasSize(3);
    }

    @Test
    public void hitCommandAndPlayerBustsRedirectsToDone() throws Exception {
        Game game = new Game(StubDeck.playerHitsAndGoesBust());
        BlackjackController blackjackController = new BlackjackController(game);
        blackjackController.startGame();

        String redirectPage = blackjackController.hitCommand();

        assertThat(redirectPage)
                .isEqualTo("redirect:/done");
    }

    @Test
    public void donePageShowsFinalGameStateWithOutcome() throws Exception {
        Game game = new Game(StubDeck.playerStandsAndBeatsDealer());
        BlackjackController blackjackController = new BlackjackController(game);
        blackjackController.startGame();
        Model model = new ConcurrentModel();

        blackjackController.doneView(model);

        assertThat(model.containsAttribute("gameView"))
                .isTrue();

        String outcome = (String) model.getAttribute("outcome");

        assertThat(outcome)
                .isNotBlank();
    }

    @Test
    public void playerStandsResultsInRedirectToDonePageAndPlayerIsDone() throws Exception {
        Game game = new Game(StubDeck.playerStandsAndBeatsDealer());
        BlackjackController blackjackController = new BlackjackController(game);
        blackjackController.startGame();

        String redirectPage = blackjackController.standCommand();

        // check the Adapter did the right thing
        assertThat(redirectPage)
                .isEqualTo("redirect:/done");
        // check that the Adapter is wired to the Application correctly
        assertThat(game.isPlayerDone())
                .isTrue();
    }


}