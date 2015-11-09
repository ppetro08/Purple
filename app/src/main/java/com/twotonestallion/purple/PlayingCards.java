package com.twotonestallion.purple;

public class PlayingCards
{
    PlayingCards(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank.getValue();
        this.cardName = rank + "_of_" + this.suit.toString();
        this.cardName = this.cardName.toLowerCase();
    }

    private Suit suit;
    private int rank;
    private String cardName;

    public Suit getSuit() {
        return suit;
    }

    public String getColor() {
        String suitColor = "";
        switch (suit) {
            case HEARTS:
                suitColor = Suit.HEARTS.getColor();
                break;
            case SPADES:
                suitColor = Suit.SPADES.getColor();
                break;
            case DIAMONDS:
                suitColor = Suit.DIAMONDS.getColor();
                break;
            case CLUBS:
                suitColor = Suit.CLUBS.getColor();
                break;
        }
        return suitColor;
    }

    public int getRank() {
        return rank;
    }

    public String getCardName() {
        return cardName;
    }
}
