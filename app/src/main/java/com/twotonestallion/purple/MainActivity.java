package com.twotonestallion.purple;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity implements FragmentOptions.OnOptionClickListener, FragmentGame.GameClickListener
{
    public Purple purple;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Add the fragment to the 'fragment_container' FrameLayout
            //getSupportFragmentManager().beginTransaction()
            //        .add(R.id.fragment_container, new FragmentGame()).commit();
            changeFragment(new FragmentGame(), null);
        }

        purple = new Purple();
        purple.initializeDeck();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGuess(String guess)
    {
        Boolean isCorrectAnswer = isCorrectAnswer(guess);
        if (isCorrectAnswer) {
            updateGameFragment();
        } else {
            changeFragment(new FragmentGame(), null);

//            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
//            String alertMessage = String.valueOf(purple.cardCount);
//            if (purple.cardCount > 1) {
//                alertMessage = alertMessage + " Drinks";
//            }
//            else {
//                alertMessage = alertMessage + " Drink";
//            }
//            alertDialog.setMessage(alertMessage);
//            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                    new DialogInterface.OnClickListener()
//                    {
//                        public void onClick(DialogInterface dialog, int which)
//                        {
//                            dialog.dismiss();
//                        }
//                    });
//            alertDialog.show();
            purple.discardCards();

//            View view = findViewById(android.R.id.content);
//            ViewOverlay viewOverlay = view.getOverlay();
//            Drawable drawable = new ColorDrawable(Color.parseColor("#ff0000"));
//            drawable.setAlpha(255);
//
//            viewOverlay.add(drawable);
//            view.setBackgroundColor(Color.parseColor("#ff0000"));
        }
    }

    // Handles the changing the cards on the Game Fragment
    public void updateGameFragment()
    {
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (frag instanceof FragmentOptions) {
            String currentCardName = purple.getCurrentCard().getCardName();
            PlayingCards previousCard = purple.getPreviousCard();
            String previousCardName = null;

            if (previousCard != null)
                previousCardName = previousCard.getCardName();

            //clearFragmentStack();

            changeFragment(new FragmentGame(), null);
            getSupportFragmentManager().executePendingTransactions();
            frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            ((FragmentGame) frag).UpdateCards(currentCardName, previousCardName, purple.cardCount);
        }
    }

    private void clearFragmentStack()
    {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
            fm.popBackStackImmediate();
        }
    }

    public Boolean isCorrectAnswer(String guess)
    {
        PlayingCards previousCard = null;
        PlayingCards currentCard = null;
        if (purple.cardCount >= 2)
            previousCard = purple.getPreviousCard();
        if (purple.cardCount >= 1)
            currentCard = purple.getCurrentCard();

        purple.flipCard();
        PlayingCards newCard = purple.getCurrentCard();
        int higherCard = 0;
        int lowerCard = 0;

        switch (guess.toUpperCase()) {
            case "RED":
                if (newCard.getColor() == "red")
                    return true;
                break;
            case "BLACK":
                if (newCard.getColor() == "black")
                    return true;
                break;
            case "HIGHER":
                if (newCard.getRank() > currentCard.getRank())
                    return true;
                break;
            case "LOWER":
                if (newCard.getRank() < currentCard.getRank())
                    return true;
                break;
            case "IN":
                if (previousCard.getRank() < currentCard.getRank()) {
                    higherCard = currentCard.getRank();
                    lowerCard = previousCard.getRank();
                } else if (previousCard.getRank() > currentCard.getRank()) {
                    higherCard = previousCard.getRank();
                    lowerCard = currentCard.getRank();
                }
                if (newCard.getRank() < higherCard && newCard.getRank() > lowerCard)
                    return true;
                break;
            case "OUT":
                if (previousCard.getRank() < currentCard.getRank()) {
                    higherCard = currentCard.getRank();
                    lowerCard = previousCard.getRank();
                } else if (previousCard.getRank() > currentCard.getRank()) {
                    higherCard = previousCard.getRank();
                    lowerCard = currentCard.getRank();
                }
                if (newCard.getRank() > higherCard || newCard.getRank() < lowerCard)
                    return true;
                break;
            case "DIAMONDS":
                if (newCard.getSuit() == Suit.DIAMONDS)
                    return true;
                break;
            case "HEARTS":
                if (newCard.getSuit() == Suit.HEARTS)
                    return true;
                break;
            case "SPADES":
                if (newCard.getSuit() == Suit.SPADES)
                    return true;
                break;
            case "CLUBS":
                if (newCard.getSuit() == Suit.CLUBS)
                    return true;
                break;
            case "PURPLE":
                purple.flipCard();
                currentCard = newCard;
                newCard = purple.getCurrentCard();
                if (currentCard.getColor() == "red" && newCard.getColor() == "black"
                        || currentCard.getColor() == "black" && newCard.getColor() == "red")
                    return true;
                break;
        }
        return false;
    }

    public void changeFragment(Fragment fragment, Bundle bundle)
    {
        if (bundle != null)
            fragment.setArguments(bundle);

        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (frag != null && frag instanceof FragmentOptions) {
            clearFragmentStack();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onGuessClick()
    {
        Bundle bundle = new Bundle();
        bundle.putInt("CardCount", purple.cardCount);

        changeFragment(new FragmentOptions(), bundle);
    }
}
