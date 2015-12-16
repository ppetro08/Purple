package com.twotonestallion.purple;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class AdManager
{
    // Static fields are shared between all instances.
    private static InterstitialAd interstitialAd;
    private Context context;

    public AdManager(Context context) {
        this.context = context;
        createAd();
    }

    public void createAd() {
        // Create an ad.
        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId("ca-app-pub-8709821789139337/4439690200");

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("AC98C820A50B4AD8A2106EDE96FB87D4").build();

        // Load the interstitial ad.
        interstitialAd.loadAd(adRequest);
    }

    public InterstitialAd getAd() {
        return interstitialAd;
    }
}
