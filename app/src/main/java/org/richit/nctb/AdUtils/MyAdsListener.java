package org.richit.nctb.AdUtils;

import android.util.Log;

public class MyAdsListener {

    private String TAG = this.getClass().getSimpleName();

    public void onAnyTrying(String which) {
        Log.d(TAG, "onAnyTrying: " + which);
        MyAllAdsUtil.setAdsLog("onAnyTrying: " + which);
    }

    public void onAnySuccess(String which) {
        Log.d(TAG, "onAnySuccess: " + which);
        MyAllAdsUtil.setAdsLog("onAnySuccess: " + which);
    }

    public void onAnyFailure(String which, int errorCode) {
        Log.d(TAG, "onAnyFailure: " + which + " Error: " + errorCode);
        MyAllAdsUtil.setAdsLog("onAnyFailure: " + which + " Error: " + errorCode);
    }

    public void onAnyClicked(String which) {
        Log.d(TAG, "onAnyClicked: " + which);
        MyAllAdsUtil.setAdsLog("onAnyClicked: " + which);
    }

    public void onAnyRewarded(String which) {
        Log.d(TAG, "onAnyRewarded: " + which);
        MyAllAdsUtil.setAdsLog("onAnyRewarded: " + which);
    }

    public void onClosed(String which) {
        Log.d(TAG, "onClosed: " + which);
        MyAllAdsUtil.setAdsLog("onClosed: " + which);
    }

    public void onTotalFailure() {
        Log.d(TAG, "onTotalFailure: ");
        MyAllAdsUtil.setAdsLog("onTotalFailure: ");
    }
}
