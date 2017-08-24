package com.softmine.dooktravel.model;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by GAURAV on 8/25/2017.
 */

public interface ILogoutIntractor {
    interface OnLogoutFinishedListener {
        void onSuccess(String response);
        void onError(String response);
        Context getAPPContext();
    }
    public void logout(JSONObject jsonObject, OnLogoutFinishedListener listener);
}
