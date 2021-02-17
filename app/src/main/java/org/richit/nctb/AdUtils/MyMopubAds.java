package org.richit.nctb.AdUtils;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;

import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubRewardedVideoListener;
import com.mopub.mobileads.MoPubRewardedVideos;
import com.mopub.mobileads.MoPubView;

public class MyMopubAds {
    private static String TAG = "MyMopubAds";

    public static class BannerAd {
        private String TAG = this.getClass().getSimpleName();

        public BannerAd(final Context context, final LinearLayout linearLayoutAds, final MoPubView.BannerAdListener listener) {
            MoPub.initializeSdk(context, new SdkConfiguration.Builder(AdHelper.getMopubAdsBanner()).build(), new SdkInitializationListener() {
                @Override
                public void onInitializationFinished() {
                    MoPubView moPubView = new MoPubView(context);
                    moPubView.setAdUnitId(AdHelper.getMopubAdsBanner());
                    moPubView.loadAd();
                    linearLayoutAds.removeAllViews();
                    linearLayoutAds.addView(moPubView);

                    if (listener != null)
                        moPubView.setBannerAdListener(listener);
                }
            });
        }

        public BannerAd(final Context context, final boolean clearAll, final LinearLayout linearLayoutAds, final MoPubView.BannerAdListener listener) {
            MoPub.initializeSdk(context, new SdkConfiguration.Builder(AdHelper.getMopubAdsBanner()).build(), new SdkInitializationListener() {
                @Override
                public void onInitializationFinished() {
                    MoPubView moPubView = new MoPubView(context);
                    moPubView.setAdUnitId(AdHelper.getMopubAdsBanner());
                    moPubView.loadAd();

                    if (clearAll)
                        linearLayoutAds.removeAllViews();
                    linearLayoutAds.addView(moPubView);

                    moPubView.setBannerAdListener(listener);
                }
            });
        }
    }

    public static class InterAd {
        MoPubInterstitial mInterstitial;

        public InterAd(final Context context, final MoPubInterstitial.InterstitialAdListener listener) {
            MoPub.initializeSdk(context, new SdkConfiguration.Builder(AdHelper.getMopubAdsBanner()).build(), new SdkInitializationListener() {
                @Override
                public void onInitializationFinished() {
                    mInterstitial = new MoPubInterstitial((Activity) context, AdHelper.getMopubAdsInter());
                    mInterstitial.load();
                    mInterstitial.setInterstitialAdListener(listener);
                }
            });
        }
    }

    public static class RewardAd {
        public RewardAd(final Context context, final MoPubRewardedVideoListener listener) {
            MoPub.initializeSdk(context, new SdkConfiguration.Builder(AdHelper.getMopubAdsBanner()).build(), new SdkInitializationListener() {
                @Override
                public void onInitializationFinished() {
                    MoPubRewardedVideos.loadRewardedVideo(AdHelper.getMopubAdsReward());
                    MoPubRewardedVideos.setRewardedVideoListener(listener);
                }
            });
        }
    }

}
