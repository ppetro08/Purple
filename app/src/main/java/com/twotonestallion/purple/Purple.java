package com.twotonestallion.purple;

import java.util.ArrayList;
import java.util.Random;

public class Purple
{
    public int numberOfDecks = 1;
    public int cardCount = 0;
    public ArrayList<PlayingCards> cards = new ArrayList<>();
    private Random rng = new Random();

    void initializeDeck()
    {
        populateDeck();
        Shuffle(cards);
    }

    public PlayingCards getCurrentCard() {
        return cards.get(cardCount - 1);
    }

    public PlayingCards getPreviousCard() {
        if (cardCount <= 1)
            return null;

        return cards.get(cardCount - 2);
    }

    public void flipCard()
    {
        cardCount++;
    }

    public void discardCards()
    {
        if (cardCount == 0)
            cards.remove(cardCount);
        else
            cards.subList(0, cardCount).clear();
        cardCount = 0;
    }

    public void populateDeck()
    {
        for (int i = 0; i < numberOfDecks; i++)
        {
            for (Rank rank : Rank.values())
            {
                for (Suit suit : Suit.values()) {
                    cards.add(new PlayingCards(suit, rank));
                }
            }
        }
    }

    public <T> void Shuffle (ArrayList<T> list)
    {
        int n = list.size();
        while (n > 1)
        {
            n--;
            int k = rng.nextInt(n + 1);
            T value = list.get(k);
            list.set(k, list.get(n));
            list.set(n, value);
        }
    }
}
