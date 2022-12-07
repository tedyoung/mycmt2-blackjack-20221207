package com.jitterted.ebp.blackjack;

public class ConsoleHand {

    // HAND (DOMAIN OBJECT) -- TRANSFORMS --> CONSOLE (String)
    static String displayFaceUpCard(Hand hand) {
        return ConsoleCard.display(hand.faceUpCard());
    }
}
