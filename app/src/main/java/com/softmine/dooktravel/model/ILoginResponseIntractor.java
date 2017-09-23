package com.softmine.dooktravel.model;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by GAURAV on 7/31/2017.
 */

public interface ILoginResponseIntractor {
    interface OnLoginFinishedListener {
        void onSuccess(String response);
        void onError(String response);
        Context getAPPContext();
    }
    public void getLoginResponse(JSONObject jsonObject, OnLoginFinishedListener listener);
    public void getLoginResponseSocail(JSONObject jsonObject, OnLoginFinishedListener listener);

}
