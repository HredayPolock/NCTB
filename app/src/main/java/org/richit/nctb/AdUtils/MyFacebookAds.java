package org.richit.nctb.AdUtils;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;

import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;

public class MyFacebookAds {
    private static String TAG = "MyFacebookAds";

    public static void init(Context context) {
        Log.d(TAG, "init: ");
        AudienceNetworkAds.initialize(context);
    }

    public static class SmallBannerAds {
        private String TAG = "MyFacebookAds: " + this.getClass().getSimpleName();

        private AdView adView;

        public SmallBannerAds(Context context, LinearLayout linearLayoutAds) {
            Log.d(TAG, "SmallBannerAds: ");
            linearLayoutAds.removeAllViews();
            this.adView = new AdView(context, AdHelper.getFacebookAdsBanner(), AdSize.BANNER_HEIGHT_50);
        }

        public SmallBannerAds(Context context, boolean clearAll, LinearLayout linearLayoutAds) {
            Log.d(TAG, "SmallBannerAds: ");
            if (clearAll) {
                linearLayoutAds.removeAllViews();
            }

            this.adView = new AdView(context, AdHelper.getFacebookAdsBanner(), AdSize.BANNER_HEIGHT_50);
            if (!AdHelper.isAdsRemoved())
                linearLayoutAds.addView(adView);
        }


        public void load(AdListener listener) {
            Log.d(TAG, "load: ");
            adView.setAdListener(listener);
            adView.loadAd();
        }
    }

    public static class InterAd {
        private String TAG = "MyFacebookAds: " + this.getClass().getSimpleName();

        private InterstitialAd interstitialAd;

        public InterAd(Context context) {
            Log.d(TAG, "InterAd: ");
            interstitialAd = new InterstitialAd(context, AdHelper.getFacebookAdsInter());
        }

        public void load(final InterstitialAdListener listener) {
            Log.d(TAG, "load: ");
            interstitialAd.loadAd();
            if (listener != null)
                interstitialAd.setAdListener(listener);
        }

        public void show() {
            Log.d(TAG, "show: " + interstitialAd.isAdLoaded());
            if (interstitialAd.isAdLoaded())
                interstitialAd.show();
        }
    }

    public static class RewardAd {
        private String TAG = "MyFacebookAds: " + this.getClass().getSimpleName();

        private RewardedVideoAd rewardedVideoAd;

        public RewardAd(Context context) {
            Log.d(TAG, "RewardAd: ");
            rewardedVideoAd = new RewardedVideoAd(context, AdHelper.getFacebookAdsReward());
        }

        public void load(RewardedVideoAdListener listener) {
            Log.d(TAG, "load: ");
            rewardedVideoAd.setAdListener(listener);
            rewardedVideoAd.loadAd();
        }

        public void show() {
            Log.d(TAG, "show: " + rewardedVideoAd.isAdLoaded());
            if (rewardedVideoAd.isAdLoaded())
                rewardedVideoAd.show();
        }
    }
}
