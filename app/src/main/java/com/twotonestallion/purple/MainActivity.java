package com.twotonestallion.purple;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AppCompatActivity implements FragmentOptions.OnOptionClickListener, FragmentGame.GameClickListener
{
    public Purple purple;
    private static Context context;

    public void createInterstitialAd()
    {
        // Creates full screen ad
        AdManager adManager = new AdManager(context);
        final InterstitialAd ad = adManager.getAd();
        ad.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded()
            {
                ad.show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        AdView mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
//                .addTestDevice("AC98C820A50B4AD8A2106EDE96FB87D4")  // An example device ID
//                .build();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

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

        if (id == R.id.rules) {
            Intent intent = new Intent(this, Rules.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGuess(String guess)
    {
        guess = guess.toUpperCase();
        Boolean isCorrectAnswer = isCorrectAnswer(guess);
        if (isCorrectAnswer) {
            updateGameFragment();
        } else {
            changeFragment(new FragmentGame(), null);

            final LinearLayout wrongCardOverlay = (LinearLayout) findViewById(R.id.wrongCardOverlay);

            String drinks = "Wrong: 1 Drink";
            int numOfDrinks = purple.cardCount;
            if (numOfDrinks > 1) {
                drinks = drinks.replace("1", String.valueOf(numOfDrinks));
                drinks += "s";
            }
            ((TextView) findViewById(R.id.lblDrinkCount)).setText(drinks);

            Drawable[] layers;
            LayerDrawable layerDrawable;
            if (guess.equalsIgnoreCase("PURPLE")) {
                layers = new Drawable[2];
                layers[1] = getDrawableCard(purple.getCurrentCard().getCardName());
                layers[0] = getDrawableCard(purple.getPreviousCard().getCardName());
                layerDrawable = new LayerDrawable(layers);
                layerDrawable.setLayerInset(1, 250, 250, 0, 0);
                layerDrawable.setLayerInset(0, 0, 0, 250, 250);
            }
            else {
                layers = new Drawable[1];
                layers[0] = getDrawableCard(purple.getCurrentCard().getCardName());
                layerDrawable = new LayerDrawable(layers);
            }
            ((ImageView) findViewById(R.id.imgWrongCard)).setImageDrawable(layerDrawable);

            wrongCardOverlay.setVisibility(View.VISIBLE);
            wrongCardOverlay.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    wrongCardOverlay.setVisibility(View.GONE);
                }
            });

            purple.discardCards();
        }
    }

    public Drawable getDrawableCard(String cardName)
    {
        String uri = "@drawable/" + cardName;
        int imageResourceID = getResources().getIdentifier(uri, null, getPackageName());
        return getResources().getDrawable(imageResourceID);
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

            changeFragment(new FragmentGame(), null);
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

        switch (guess) {
            case "RED":
                if (newCard.getColor().equals("red"))
                    return true;
                break;
            case "BLACK":
                if (newCard.getColor().equals("black"))
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
                if (previousCard.getRank() == currentCard.getRank())
                    return false;
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
                if (previousCard.getRank() == currentCard.getRank())
                    return false;
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
                if (newCard.getSuit().equals(Suit.DIAMONDS))
                    return true;
                break;
            case "HEARTS":
                if (newCard.getSuit().equals(Suit.HEARTS))
                    return true;
                break;
            case "SPADES":
                if (newCard.getSuit().equals(Suit.SPADES))
                    return true;
                break;
            case "CLUBS":
                if (newCard.getSuit().equals(Suit.CLUBS))
                    return true;
                break;
            case "PURPLE":
                purple.flipCard();
                currentCard = newCard;
                newCard = purple.getCurrentCard();
                if (currentCard.getColor().equals("red") && newCard.getColor().equals("black")
                        || currentCard.getColor().equals("black") && newCard.getColor().equals("red"))
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

        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public void onGuessClick()
    {
        Bundle bundle = new Bundle();
        bundle.putInt("CardCount", purple.cardCount);

        changeFragment(new FragmentOptions(), bundle);
    }

    // Changes back button functionality based on the current fragment
    @Override
    public void onBackPressed()
    {

        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (frag instanceof FragmentOptions) {
            super.onBackPressed();
        } else if (frag instanceof FragmentGame) {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }
}
