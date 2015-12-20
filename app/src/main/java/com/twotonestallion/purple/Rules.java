package com.twotonestallion.purple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class Rules extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        String rules = "" +
                "<div style=\"font-size:19px\">" +
                    "<p>Purple is a group drinking game that has one dealer and one guesser. To " +
                    "start a round the dealer clicks the card and asks the player to their left to guess " +
                    "based on the options given. If a player guesses correctly they may pass to the next " +
                    "player on their left or continue to guess. When a card is incorrectly guessed that player " +
                    "must take a drink for each card on the drawn and the new dealer is the player to the left of the dealer</p>" +
                    "<p style=\"margin-bottom:0; padding-bottom:0;\">Additional Rules:</p>" +
                    "<ul style=\"margin-top:0; padding-top:0;\">" +
                        "<li>Aces are high</li>" +
                        "<li>In: Inbetween but not including the two cards</li>" +
                        "<li>Out: Outside but not including the two cards</li>" +
                        "<li>Purple: A red and black card or a black and red card consecutively.</li>" +
                    "</ul>" +
                "</div>";
        WebView webView;
        webView = (WebView) findViewById(R.id.webView);
        webView.setBackgroundColor(getResources().getColor(R.color.colorBackground));
        webView.loadData(rules, "text/html", null);
    }
}
