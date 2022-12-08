package com.jitterted.ebp.blackjack.domain;

public class Game {

    private final Deck deck;

    private final Hand dealerHand = new Hand();
    private final Hand playerHand = new Hand();
    private boolean playerDone;

    public Game(Deck deck) {
        this.deck = deck;
    }

    public void initialDeal() {
        dealRoundOfCards();
        dealRoundOfCards();
    }

    private void dealRoundOfCards() {
        // why: players first because this is the rule
        playerHand.drawFrom(deck);
        dealerHand.drawFrom(deck);
    }

    public String determineOutcome() {
        if (playerHand.isBusted()) {
            return "You Busted, so you lose.  💸";
        } else if (dealerHand.isBusted()) {
            return "Dealer went BUST, Player wins! Yay for you!! 💵";
        } else if (playerHand.beats(dealerHand)) {
            return "You beat the Dealer! 💵";
        } else if (playerHand.pushes(dealerHand)) {
            return "Push: Nobody wins, we'll call it even.";
        } else {
            return "You lost to the Dealer. 💸";
        }
    }

    public void dealerTurn() {
        // Dealer makes its choice automatically based on a simple heuristic (<=16 must hit, =>17 must stand)
        if (!playerHand.isBusted()) {
            while (dealerHand.dealerMustDrawCard()) {
                dealerHand.drawFrom(deck);
            }
        }
    }

    public Hand playerHand() {
        return playerHand;
    }

    // QUERY RULES: What options do we have?
    // -> Snapshot
    // -> Integrity/Encapsulation
    // -> Immutable
    // -> Not mislead caller
    // Options:
    // 1. Return copy of Hand: misleads caller
    // 2. List<Card>/Stream<Card>: doesn't have the value() nor faceUpCard() method/behavior
    // ==> 3. New Class: HandView ==> Value Object
    // 4. Interface: ReadOnlyHand -> wrap the underlying Hand, but NOT a Snapshot
    public Hand dealerHand() {
        return dealerHand; // make a copy
    }

    public boolean isPlayerDone() {
        return playerDone;
    }

    public void playerHits() {
        playerHand.drawFrom(deck);
        playerDone = playerHand.isBusted();
    }

    public void playerStands() {
        playerDone = true;
    }

}
