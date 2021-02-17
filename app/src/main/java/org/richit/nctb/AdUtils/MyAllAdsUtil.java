package org.richit.nctb.AdUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.RewardedVideoAdExtendedListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.mopub.common.MoPubReward;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubRewardedVideoListener;
import com.mopub.mobileads.MoPubRewardedVideos;
import com.mopub.mobileads.MoPubView;
import com.startapp.android.publish.ads.banner.BannerListener;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.VideoListener;
import com.startapp.android.publish.adsCommon.adListeners.AdDisplayListener;

import org.richit.nctb.R;

import java.util.Set;

public class MyAllAdsUtil {
    private static String TAG = "MyAllAdsUtil";

    //
    private static String adsLog = "";
    private static int bannerAdsTried = 0;
    private static int interAdsTried = 0;
    private static int rewardAdsTried = 0;

    //
    public static String getAdsLog() {
        return MyAllAdsUtil.adsLog;
    }

    public static void setAdsLog(String adsLog) {
        MyAllAdsUtil.adsLog += adsLog + "\n";
    }

    //
    public static void setBannerAdsTriedToZero() {
        MyAllAdsUtil.bannerAdsTried = 0;
    }

    public static void setInterAdsTriedToZero() {
        MyAllAdsUtil.interAdsTried = 0;
    }

    public static void setRewardAdsTriedToZero() {
        MyAllAdsUtil.rewardAdsTried = 0;
    }

    //
    public static void setBannerAdsTried(int bannerAdsTried) {
        MyAllAdsUtil.bannerAdsTried = bannerAdsTried;
    }

    public static void setInterAdsTried(int interAdsTried) {
        MyAllAdsUtil.interAdsTried = interAdsTried;
    }

    public static void setRewardAdsTried(int rewardAdsTried) {
        MyAllAdsUtil.rewardAdsTried = rewardAdsTried;
    }

    //
    public static String GOOGLE = "GOOGLE";
    public static String MOPUB = "MOPUB";
    public static String FACEBOOK = "FACEBOOK";
    public static String STARTAPP = "STARTAPP";

    //
    public static void init(Context context) {
        MyGoogleAds.init(context);
        MyFacebookAds.init(context);
        MyStartAppAds.init((Activity) context, null, 0, false);
    }

    public static void init(Context context, boolean showStartappReturnAd) {
        MyGoogleAds.init(context);
        MyFacebookAds.init(context);
        MyStartAppAds.init((Activity) context, null, 0, showStartappReturnAd);
    }

    public static void init(Context context, Bundle savedInstanceState, int splash_screen, boolean showStartappReturnAd) {
        MyGoogleAds.init(context);
        MyFacebookAds.init(context);
        MyStartAppAds.init((Activity) context, savedInstanceState, splash_screen, showStartappReturnAd);
    }

    //
    public static void addAnyBannerAd(Context context, LinearLayout linearLayout) {
        addAnyBannerAd(true, context, true, linearLayout, new MyAdsListener());
    }

    //
    public static void addAnyBannerAd(boolean restart, Context context, boolean clearAll, LinearLayout linearLayout, MyAdsListener listener) {
        if (restart)
            setBannerAdsTriedToZero();

        bannerAdsTried++;
        switch (bannerAdsTried) {
            case 1:
                addGoogleBannerAd(true, context, clearAll, linearLayout, listener);
                break;

            case 2:
                addFacebookBannerAd(true, context, clearAll, linearLayout, listener);
                break;

            case 3:
                addMopubBannerAd(true, context, clearAll, linearLayout, listener);
                break;

            case 4:
                addStartappBannerAd(true, context, clearAll, linearLayout, listener);
                break;

            case 5:
                addBigStartappAds(false, context, clearAll, linearLayout, listener);
                break;
        }
    }

    public static void addAllBannerAds(Context context, LinearLayout linearLayout, MyAdsListener listener) {
        addGoogleBannerAd(false, context, false, linearLayout, listener);
        addFacebookBannerAd(false, context, false, linearLayout, listener);
        addMopubBannerAd(false, context, false, linearLayout, listener);
        addStartappBannerAd(false, context, false, linearLayout, listener);
        addBigStartappAds(false, context, false, linearLayout, listener);
    }

    //
    public static void addGoogleBannerAd(final boolean next, final Context context, final boolean clearAll, final LinearLayout linearLayout, final MyAdsListener listener) {
        listener.onAnyTrying(GOOGLE);
        new MyGoogleAds.BannerAd(context, clearAll, linearLayout).load(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                listener.onAnyFailure(GOOGLE, i);

                if (next) {
                    addAnyBannerAd(false, context, clearAll, linearLayout, listener);
                } else {
                    listener.onTotalFailure();
                }
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                listener.onClosed(GOOGLE);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                listener.onAnyClicked(GOOGLE);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                listener.onAnySuccess(GOOGLE);
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                listener.onAnyClicked(GOOGLE);
            }
        });
    }

    public static void addFacebookBannerAd(final boolean next, final Context context, final boolean clearAll, final LinearLayout linearLayout, final MyAdsListener listener) {
        listener.onAnyTrying(FACEBOOK);
        new MyFacebookAds.SmallBannerAds(context, clearAll, linearLayout).load(new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                listener.onAnyFailure(FACEBOOK, adError.getErrorCode());

                if (next) {
                    addAnyBannerAd(false, context, clearAll, linearLayout, listener);
                } else {
                    listener.onTotalFailure();
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                listener.onAnySuccess(FACEBOOK);
            }

            @Override
            public void onAdClicked(Ad ad) {
                listener.onAnyClicked(FACEBOOK);
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                //
            }
        });
    }

    public static void addMopubBannerAd(final boolean next, final Context context, final boolean clearAll, final LinearLayout linearLayout, final MyAdsListener listener) {
        listener.onAnyTrying(MOPUB);
        new MyMopubAds.BannerAd(context, clearAll, linearLayout, new MoPubView.BannerAdListener() {
            @Override
            public void onBannerLoaded(MoPubView banner) {
                listener.onAnySuccess(MOPUB);
            }

            @Override
            public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
                listener.onAnyFailure(MOPUB, errorCode.getIntCode());

                if (next) {
                    addAnyBannerAd(false, context, clearAll, linearLayout, listener);
                } else {
                    listener.onTotalFailure();
                }
            }

            @Override
            public void onBannerClicked(MoPubView banner) {
                listener.onAnyClicked(MOPUB);
            }

            @Override
            public void onBannerExpanded(MoPubView banner) {
                //
            }

            @Override
            public void onBannerCollapsed(MoPubView banner) {
                //
            }
        });
    }

    public static void addStartappBannerAd(final boolean next, final Context context, final boolean clearAll, final LinearLayout linearLayout, final MyAdsListener listener) {
        listener.onAnyTrying(STARTAPP);
        new MyStartAppAds.BannerAd((Activity) context, linearLayout, clearAll, new BannerListener() {
            @Override
            public void onReceiveAd(View view) {
                listener.onAnySuccess(STARTAPP);
            }

            @Override
            public void onFailedToReceiveAd(View view) {
                listener.onAnyFailure(STARTAPP, -99);

                if (next) {
                    addAnyBannerAd(false, context, clearAll, linearLayout, listener);
                } else {
                    listener.onTotalFailure();
                }
            }

            @Override
            public void onClick(View view) {
                listener.onAnyClicked(STARTAPP);
            }
        });
    }

    public static void addBigStartappAds(final boolean next, final Context context, final boolean clearAll, final LinearLayout linearLayout, final MyAdsListener listener) {
        listener.onAnyTrying(STARTAPP + "-BIG");
        new MyStartAppAds.MrecAd((Activity) context, linearLayout, clearAll, new BannerListener() {
            @Override
            public void onReceiveAd(View view) {
                listener.onAnySuccess(STARTAPP + "-BIG");
            }

            @Override
            public void onFailedToReceiveAd(View view) {
                listener.onAnyFailure(STARTAPP + "-BIG", -99);
            }

            @Override
            public void onClick(View view) {
                listener.onAnyClicked(STARTAPP + "-BIG");
            }
        });

        new MyStartAppAds.CoverAd((Activity) context, linearLayout, clearAll, new BannerListener() {
            @Override
            public void onReceiveAd(View view) {
                listener.onAnySuccess(STARTAPP + "-BIG");
            }

            @Override
            public void onFailedToReceiveAd(View view) {
                listener.onAnyFailure(STARTAPP + "-BIG", -99);
            }

            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                listener.onAnyClicked(STARTAPP + "-BIG");
            }
        });
    }

    public static boolean showInterIfOk(Context context, final MyAdsListener listener) {
        if (AdHelper.shouldShowInter()) {
            showAnyInter(true, context, listener);
            return true;
        }
        return false;
    }

    //
    public static void showAnyInter(boolean restart, Context context, final MyAdsListener listener) {
        if (restart)
            setInterAdsTriedToZero();
        interAdsTried++;

        switch (interAdsTried) {
            case 1:
                showGoogleInter(true, context, listener);
                break;

            case 2:
                showFacebookInter(true, context, listener);
                break;

            case 3:
                showMopubInter(true, context, listener);
                break;

            case 4:
                showStartappInter(false, context, listener);
                break;
        }
    }

    public static void showGoogleInter(final boolean next, final Context context, final MyAdsListener listener) {
        listener.onAnyTrying(GOOGLE);
        final MyGoogleAds.InterAd interAd = new MyGoogleAds.InterAd(context);
        interAd.load(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                listener.onAnyFailure(GOOGLE, i);

                if (next) {
                    showAnyInter(false, context, listener);
                } else {
                    listener.onTotalFailure();
                }
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                listener.onAnyClicked(GOOGLE);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                listener.onAnySuccess(GOOGLE);
                interAd.show();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                listener.onAnyClicked(GOOGLE);
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                listener.onClosed(GOOGLE);
            }
        });
    }

    public static void showFacebookInter(final boolean next, final Context context, final MyAdsListener listener) {
        listener.onAnyTrying(FACEBOOK);
        final MyFacebookAds.InterAd interAd = new MyFacebookAds.InterAd(context);
        interAd.load(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                //
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                listener.onClosed(FACEBOOK);
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                listener.onAnyFailure(FACEBOOK, adError.getErrorCode());

                if (next) {
                    showAnyInter(false, context, listener);
                } else {
                    listener.onTotalFailure();
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                listener.onAnySuccess(FACEBOOK);
                interAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                listener.onAnyClicked(FACEBOOK);
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                //
            }
        });
    }

    public static void showMopubInter(final boolean next, final Context context, final MyAdsListener listener) {
        listener.onAnyTrying(MOPUB);
        new MyMopubAds.InterAd(context, new MoPubInterstitial.InterstitialAdListener() {
            @Override
            public void onInterstitialLoaded(MoPubInterstitial interstitial) {
                listener.onAnySuccess(MOPUB);
                interstitial.show();
            }

            @Override
            public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
                listener.onAnyFailure(MOPUB, errorCode.getIntCode());

                if (next) {
                    showAnyInter(false, context, listener);
                } else {
                    listener.onTotalFailure();
                }
            }

            @Override
            public void onInterstitialShown(MoPubInterstitial interstitial) {
                //
            }

            @Override
            public void onInterstitialClicked(MoPubInterstitial interstitial) {
                listener.onAnyClicked(MOPUB);
            }

            @Override
            public void onInterstitialDismissed(MoPubInterstitial interstitial) {
                listener.onClosed(MOPUB);
            }
        });
    }

    public static void showStartappInter(final boolean next, final Context context, final MyAdsListener listener) {
        listener.onAnyTrying(STARTAPP);
        new MyStartAppAds.InterAd(context, new VideoListener() {
            @Override
            public void onVideoCompleted() {
                //
            }
        }, new AdDisplayListener() {
            @Override
            public void adHidden(com.startapp.android.publish.adsCommon.Ad ad) {
                listener.onClosed(STARTAPP);
            }

            @Override
            public void adDisplayed(com.startapp.android.publish.adsCommon.Ad ad) {
                listener.onAnySuccess(STARTAPP);
            }

            @Override
            public void adClicked(com.startapp.android.publish.adsCommon.Ad ad) {
                listener.onAnyClicked(STARTAPP);
            }

            @Override
            public void adNotDisplayed(com.startapp.android.publish.adsCommon.Ad ad) {
                listener.onAnyFailure(STARTAPP, -99);

                if (next) {
                    showAnyInter(false, context, listener);
                } else {
                    listener.onTotalFailure();
                }
            }
        }).show();
    }

    //
    public static void showAnyReward(boolean restart, Context context, final MyAdsListener listener) {
        if (restart)
            setRewardAdsTriedToZero();
        rewardAdsTried++;

        switch (rewardAdsTried) {
            case 1:
                showGoogleReward(true, context, listener);
                break;

            case 2:
                showFacebookReward(true, context, listener);
                break;

            case 3:
                showMopubReward(true, context, listener);
                break;

            case 4:
                showStartappReward(true, context, listener);
                break;
        }
    }

    public static void showGoogleReward(final boolean next, final Context context, final MyAdsListener listener) {
        listener.onAnyTrying(GOOGLE);
        final MyGoogleAds.RewardAd rewardAd = new MyGoogleAds.RewardAd(context);
        rewardAd.load(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                listener.onAnySuccess(GOOGLE);
                rewardAd.show();
            }

            @Override
            public void onRewardedVideoAdOpened() {
                //
            }

            @Override
            public void onRewardedVideoStarted() {
                //
            }

            @Override
            public void onRewardedVideoAdClosed() {
                listener.onClosed(GOOGLE);
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                listener.onAnyRewarded(GOOGLE);
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                listener.onAnyClicked(GOOGLE);
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                listener.onAnyFailure(GOOGLE, i);

                if (next) {
                    showAnyReward(false, context, listener);
                } else {
                    listener.onTotalFailure();
                }
            }

            @Override
            public void onRewardedVideoCompleted() {
                //
            }
        });
    }

    public static void showFacebookReward(final boolean next, final Context context, final MyAdsListener listener) {
        listener.onAnyTrying(FACEBOOK);
        final MyFacebookAds.RewardAd rewardAd = new MyFacebookAds.RewardAd(context);
        rewardAd.load(new RewardedVideoAdExtendedListener() {
            @Override
            public void onRewardedVideoActivityDestroyed() {
                //
            }

            @Override
            public void onRewardedVideoCompleted() {
                listener.onAnyRewarded(FACEBOOK);
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                //
            }

            @Override
            public void onRewardedVideoClosed() {
                listener.onClosed(FACEBOOK);
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                listener.onAnyFailure(FACEBOOK, adError.getErrorCode());

                if (next) {
                    showAnyReward(false, context, listener);
                } else {
                    listener.onTotalFailure();
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                listener.onAnySuccess(FACEBOOK);
                rewardAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                listener.onAnyClicked(FACEBOOK);
            }
        });
    }

    public static void showMopubReward(final boolean next, final Context context, final MyAdsListener listener) {
        listener.onAnyTrying(MOPUB);
        new MyMopubAds.RewardAd(context, new MoPubRewardedVideoListener() {
            @Override
            public void onRewardedVideoLoadSuccess(@NonNull String adUnitId) {
                listener.onAnySuccess(MOPUB);
                MoPubRewardedVideos.showRewardedVideo(adUnitId);
            }

            @Override
            public void onRewardedVideoLoadFailure(@NonNull String adUnitId, @NonNull MoPubErrorCode errorCode) {
                listener.onAnyFailure(MOPUB, errorCode.getIntCode());

                if (next) {
                    showAnyReward(false, context, listener);
                } else {
                    listener.onTotalFailure();
                }
            }

            @Override
            public void onRewardedVideoStarted(@NonNull String adUnitId) {
                //
            }

            @Override
            public void onRewardedVideoPlaybackError(@NonNull String adUnitId, @NonNull MoPubErrorCode errorCode) {
                listener.onAnyFailure(MOPUB, errorCode.getIntCode());

                if (next) {
                    showAnyReward(false, context, listener);
                } else {
                    listener.onTotalFailure();
                }
            }

            @Override
            public void onRewardedVideoClicked(@NonNull String adUnitId) {
                listener.onAnyClicked(MOPUB);
            }

            @Override
            public void onRewardedVideoClosed(@NonNull String adUnitId) {
                listener.onClosed(MOPUB);
            }

            @Override
            public void onRewardedVideoCompleted(@NonNull Set<String> adUnitIds, @NonNull MoPubReward reward) {
                listener.onAnyRewarded(MOPUB);
            }
        });
    }

    public static void showStartappReward(final boolean next, final Context context, final MyAdsListener listener) {
        listener.onAnyTrying(STARTAPP);
        new MyStartAppAds.InterAd(context, new VideoListener() {
            @Override
            public void onVideoCompleted() {
                listener.onAnyRewarded(STARTAPP);
            }
        }, new AdDisplayListener() {
            @Override
            public void adHidden(com.startapp.android.publish.adsCommon.Ad ad) {
                listener.onClosed(STARTAPP);
            }

            @Override
            public void adDisplayed(com.startapp.android.publish.adsCommon.Ad ad) {
                listener.onAnySuccess(STARTAPP);
            }

            @Override
            public void adClicked(com.startapp.android.publish.adsCommon.Ad ad) {
                listener.onAnyClicked(STARTAPP);
            }

            @Override
            public void adNotDisplayed(com.startapp.android.publish.adsCommon.Ad ad) {
                listener.onAnyFailure(STARTAPP, -99);

                if (next) {
                    showAnyReward(false, context, listener);
                } else {
                    listener.onTotalFailure();
                }
            }
        }, StartAppAd.AdMode.REWARDED_VIDEO);
    }

    //
    public static void addBannerAdsInDialog(Context context, AlertDialog.Builder builder) {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_ad_container, null);
        builder.setView(view);

        LinearLayout linearLayout = view.findViewById(R.id.adsLL);
        setBannerAdsTried(1);
        addAnyBannerAd(false, context, true, linearLayout, new MyAdsListener());
    }

    //
    private static int shareClickCount = 0;

    public static void showAdsLogDialog(Context context) {
        if (++shareClickCount % 4 == 0) {
            new AlertDialog.Builder(context)
                    .setTitle("Ads LOG")
                    .setMessage("" + getAdsLog())
                    .show();
        }
    }
}
