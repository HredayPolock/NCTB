package org.richit.nctb.Others;

import org.richit.nctb.AdUtils.AdHelper;
import org.richit.nctb.Config;

public class GlobalMethods {
    public static void initAdIds(String gs) {
        String adIds[] = AdHelper.decrypt(gs).split(",");

        // If you have NOT added all the ads IDs ( Banner, Interstitial, Reward ) for Google Admob
        // We will provide some dummy ads ID from the internet so that it doesn't stay blank
        if (Config.admobAppId.isEmpty() || Config.admobBannerAds.isEmpty() ||
                Config.admobInterAds.isEmpty() || Config.admobRewardAds.isEmpty()) {

            // These IDs come from the internet as BASE64 encripted values
            int i = 0;
            AdHelper.initAdmobIdsEnc(
                    adIds[i++],
                    adIds[i++],
                    adIds[i++],
                    adIds[i++]
            );
        }

        // But if you have added all the IDs for Google Admob
        else {
            AdHelper.initAdmobIds(
                    Config.admobAppId,
                    Config.admobBannerAds,
                    Config.admobInterAds,
                    Config.admobRewardAds
            );

            // But If the IDs are encripted (Base64), uncomment the lines below and comment the lines above
//            AdHelper.initAdmobIdsEnc(
//                    Config.admobAppId,
//                    Config.bannerAds,
//                    Config.interAds,
//                    Config.rewardAds
//            );
        }

        if (Config.facebookAdsBanner.isEmpty() || Config.facebookAdsInter.isEmpty() ||
                Config.facebookAdsReward.isEmpty()) {
            int i = 0;
            // These IDs come from the internet as BASE64 encripted values
            AdHelper.initFacebookIdsEnc(
                    adIds[i++],
                    adIds[i++],
                    adIds[i++]
            );

        } else {
            // But if you have added all the IDs for Facebook Ads
            AdHelper.initFacebookIds(
                    Config.facebookAdsBanner,
                    Config.facebookAdsInter,
                    Config.facebookAdsReward

            );

            // But If the IDs are encripted (Base64), uncomment the lines below and comment the lines above
//          AdHelper.initFacebookIdsEnc(
//                    Config.facebookAdsBanner,
//                    Config.facebookAdsInter,
//                    Config.facebookAdsReward
//            );
        }
        if (Config.mopubAdsBanner.isEmpty() ||
                Config.mopubAdsInter.isEmpty() ||
                Config.mopubAdsReward.isEmpty()) {
            int i = 0;
            // These IDs come from the internet as BASE64 encripted values
            AdHelper.initMopubIdsEnc(
                    adIds[i++],
                    adIds[i++],
                    adIds[i++]
            );
        } else {
            // But if you have added all the IDs for Mopub Ads
            AdHelper.initMopubIds(
                    Config.mopubAdsBanner,
                    Config.mopubAdsInter,
                    Config.mopubAdsReward
            );
            // But If the IDs are encripted (Base64), uncomment the lines below and comment the lines above
//          AdHelper.initMopubIdsEnc(
//                   Config.mopubAdsBanner,
//                    Config.mopubAdsInter,
//                    Config.mopubAdsReward
//            );
        }

        // Same goes for StartApp. If you don't add any ads ID for startapp, we provide a dummy Ad ID from the internet
        if (Config.startAppAds.isEmpty()) {
            AdHelper.initStartappIdEnc(adIds[4]);
        }

        // If you have added all the ID for StartApp
        else {
            AdHelper.initStartappId(Config.startAppAds);

            // But If the ID is encripted (Base64), uncomment the line below and comment the lines above
           // AdHelper.initStartappIdEnc(Config.startAppAds);
        }


    }
}
