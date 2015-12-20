package com.twotonestallion.purple_paid;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentGame extends Fragment implements View.OnClickListener
{
    GameClickListener gameClickListener;
    private View view;
    private String CurrentCard = null;
    private String PreviousCard = null;
    private int CardCount = 0;

    public interface GameClickListener
    {
        void onGuessClick();
    }

    public void UpdateCards(String currentCard, String previousCard, int cardCount)
    {
        CurrentCard = currentCard;
        PreviousCard = previousCard;
        CardCount = cardCount;

        int layersSize = cardCount >= 2 ? 2 : 1;
        Drawable[] layers = new Drawable[layersSize];

        layers[0] = getDrawableCard(CurrentCard);
        if (layersSize == 2) {
            layers[1] = getDrawableCard(CurrentCard);
            layers[0] = getDrawableCard(PreviousCard);
        }

        LayerDrawable layerDrawable = new LayerDrawable(layers);
        if (layersSize == 2) {
            layerDrawable.setLayerInset(1, 250, 250, 0, 0);
            layerDrawable.setLayerInset(0, 0, 0, 250, 250);
        }

        ((ImageView) view.findViewById(R.id.imgCards)).setImageDrawable(layerDrawable);
        ((TextView) view.findViewById(R.id.lblCount)).setText(String.valueOf(CardCount));
    }

    @Override
    public void onCreate(Bundle savedBundleState)
    {
        super.onCreate(savedBundleState);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            gameClickListener = (GameClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement gameClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_game, container, false);
        view.findViewById(R.id.imgCards).setOnClickListener(this);

        if (CurrentCard != null) {
            UpdateCards(CurrentCard, PreviousCard, CardCount);
        } else {
            ((ImageView) view.findViewById(R.id.imgCards)).setImageDrawable(getDrawableCard("back_of_card"));
        }

        ((TextView) view.findViewById(R.id.lblCount)).setText(String.valueOf(CardCount));
        return view;
    }

    @Override
    public void onClick(View v)
    {
        gameClickListener.onGuessClick();
    }

    public Drawable getDrawableCard(String cardName)
    {
        String uri = "@drawable/" + cardName;
        int imageResourceID = getResources().getIdentifier(uri, null, getActivity().getPackageName());
        return getResources().getDrawable(imageResourceID);
    }
}
