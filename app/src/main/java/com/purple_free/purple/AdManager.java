package com.purple_free.purple;

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
        interstitialAd.setAdUnitId(context.getString(R.string.interstitial_ad_unit_id));

//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .addTestDevice("AC98C820A50B4AD8A2106EDE96FB87D4").build();
        AdRequest adRequest = new AdRequest.Builder().build();
        // Load the interstitial ad.
        interstitialAd.loadAd(adRequest);
    }

    public InterstitialAd getAd() {
        return interstitialAd;
    }
}