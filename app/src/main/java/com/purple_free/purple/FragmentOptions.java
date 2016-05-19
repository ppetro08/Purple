package com.purple_free.purple;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentOptions extends Fragment implements View.OnClickListener
{
    OnOptionClickListener onOptionListener;

    public interface OnOptionClickListener
    {
        void onGuess(String guess);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            onOptionListener = (OnOptionClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnGuessListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_options, container, false);
        Bundle bundle = this.getArguments();
        int cardCount = bundle.getInt("CardCount");


        Button btnBlack = (Button) view.findViewById(R.id.btnBlack);
        Button btnRed = (Button) view.findViewById(R.id.btnRed);
        Button btnHigher = (Button) view.findViewById(R.id.btnHigher);
        Button btnLower = (Button) view.findViewById(R.id.btnLower);
        Button btnIn = (Button) view.findViewById(R.id.btnIn);
        Button btnOut = (Button) view.findViewById(R.id.btnOut);
        Button btnDiamonds = (Button) view.findViewById(R.id.btnDiamonds);
        Button btnSpades = (Button) view.findViewById(R.id.btnSpades);
        Button btnHearts = (Button) view.findViewById(R.id.btnHearts);
        Button btnClubs = (Button) view.findViewById(R.id.btnClubs);
        Button btnPurple = (Button) view.findViewById(R.id.btnPurple);

        btnBlack.setOnClickListener(this);
        btnRed.setOnClickListener(this);
        btnDiamonds.setOnClickListener(this);
        btnSpades.setOnClickListener(this);
        btnHearts.setOnClickListener(this);
        btnClubs.setOnClickListener(this);
        btnPurple.setOnClickListener(this);

        if (cardCount > 0) {
            btnHigher.setOnClickListener(this);
            btnLower.setOnClickListener(this);
            btnIn.setOnClickListener(this);
            btnOut.setOnClickListener(this);
        }
        else {
            btnHigher.setClickable(false);
            btnLower.setClickable(false);

            btnHigher.setVisibility(View.GONE);
            btnLower.setVisibility(View.GONE);
        }

        if (cardCount > 1) {
            btnIn.setOnClickListener(this);
            btnOut.setOnClickListener(this);
        }
        else {
            btnIn.setClickable(false);
            btnOut.setClickable(false);

            btnIn.setVisibility(View.GONE);
            btnOut.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onClick(View v)
    {
        String buttonName = ((Button)v).getText().toString();
        onOptionListener.onGuess(buttonName);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        onOptionListener = null;
    }
}
