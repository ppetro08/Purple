package com.twotonestallion.purple;

import java.util.ArrayList;
import java.util.Random;

public class Purple
{
    public int cardCount;
    private ArrayList<PlayingCards> cards;
    private Random rng = new Random();

    void initializeDeck()
    {
        if (cards == null)
            cards = new ArrayList<>();

        ArrayList<PlayingCards> newCards;
        newCards = populateDeck();
        newCards = Shuffle(newCards);
        cards.addAll(newCards);
    }

    public PlayingCards getCurrentCard()
    {
        return cards.get(cardCount - 1);
    }

    public PlayingCards getPreviousCard()
    {
        if (cardCount <= 1)
            return null;

        return cards.get(cardCount - 2);
    }

    public void flipCard()
    {
        cardCount++;
        if (cardCount > cards.size()) {
            initializeDeck();
            MainActivity mainActivity = new MainActivity();
            mainActivity.createInterstitialAd(); // Creates full screen ad
            cardCount++;
        }
    }

    public void discardCards()
    {
        if (cardCount == 0)
            cards.remove(cardCount);
        else
            cards.subList(0, cardCount).clear();
        cardCount = 0;
    }

    public ArrayList<PlayingCards> populateDeck()
    {
        for (Rank rank : Rank.values()) {
            for (Suit suit : Suit.values()) {
                cards.add(new PlayingCards(suit, rank));
            }
        }
        return cards;
    }

    private <T> ArrayList<T> Shuffle(ArrayList<T> list)
    {
        int n = list.size();
        while (n > 1) {
            n--;
            int k = rng.nextInt(n + 1);
            T value = list.get(k);
            list.set(k, list.get(n));
            list.set(n, value);
        }
        return list;
    }
}
