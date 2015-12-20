package com.twotonestallion.purple_paid;

public enum Suit
{
    HEARTS("red"),
    SPADES("black"),
    DIAMONDS("red"),
    CLUBS("black");

    private final String text;

    Suit(final String text) {
        this.text = text;
    }

    public String getColor() {
        return text;
    }
}
