package com.softmine.dooktravel.model;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by GAURAV on 9/14/2017.
 */

public interface IOTPResendIntractor {
    interface OnOTPResendFinishedListener {
        void onSuccess(String response);
        void onError(String response);
        Context getAPPContext();
    }
    public void resend(JSONObject jsonObject, OnOTPResendFinishedListener listener);
}
