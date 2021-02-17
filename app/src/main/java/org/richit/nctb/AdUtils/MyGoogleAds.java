package org.richit.nctb.AdUtils;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class MyGoogleAds {
    private static String TAG = "MyGoogleAds";

    public static void init(Context context) {
        Log.d(TAG, "MyGoogleAds: ");
        MobileAds.initialize(context, AdHelper.getAdmobAppId());
    }

    public static class BannerAd {
        private String TAG = "MyGoogleAds: " + this.getClass().getSimpleName();

        private AdView adView;
        private LinearLayout linearLayoutAds;
        private AdRequest adRequest;
        private boolean clearAll = true;

        public BannerAd(Context context, LinearLayout linearLayoutAds) {
            Log.d(TAG, "BannerAd: ");
            this.linearLayoutAds = linearLayoutAds;

            adView = new AdView(context);
            adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdUnitId(AdHelper.getAdmobBannerAdsId());
        }

        public BannerAd(Context context, boolean clearAll, LinearLayout linearLayoutAds) {
            Log.d(TAG, "BannerAd: ");
            this.linearLayoutAds = linearLayoutAds;
            this.clearAll = clearAll;

            adView = new AdView(context);
            adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdUnitId(AdHelper.getAdmobBannerAdsId());
        }


        public void load(AdListener listener) {
            adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

            if (clearAll) {
                linearLayoutAds.removeAllViews();
            }

            linearLayoutAds.addView(adView);
            adView.setAdListener(listener);
        }
    }

    public static class InterAd {
        private String TAG = "MyGoogleAds: " + this.getClass().getSimpleName();

        public InterstitialAd interstitialAd;

        public InterAd(Context context) {
            Log.d(TAG, "InterAd: ");

            interstitialAd = new InterstitialAd(context);
            interstitialAd.setAdUnitId(AdHelper.getAdmobInterAdsId());
        }

        public void load(AdListener listener) {
            Log.d(TAG, "load: ");
            interstitialAd.loadAd(new AdRequest.Builder().build());
            interstitialAd.setAdListener(listener);
        }

        public void show() {
            Log.d(TAG, "show: ");
            if (interstitialAd.isLoaded()) {
                interstitialAd.show();
            }
        }
    }

    public static class RewardAd {
        private String TAG = "MyGoogleAds: " + this.getClass().getSimpleName();

        public RewardedVideoAd rewardedVideoAd;

        public RewardAd(Context context) {
            Log.d(TAG, "RewardAd: ");
            rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
        }

        public void load(RewardedVideoAdListener listener) {
            Log.d(TAG, "load: ");
            rewardedVideoAd.loadAd(AdHelper.getAdmobRewardAdsId(), new AdRequest.Builder().build());
            rewardedVideoAd.setRewardedVideoAdListener(listener);
        }

        public void show() {
            Log.d(TAG, "show: ");
            if (rewardedVideoAd.isLoaded()) {
                rewardedVideoAd.show();
            }
        }
    }
}
