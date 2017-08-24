package com.softmine.dooktravel.model;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by GAURAV on 8/23/2017.
 */

public interface IChangePasswordResponseIntractor {
    interface OnChangePasswordResponseFinishedListener {
        void onSuccess(String response);
        void onError(String response);
        Context getAPPContext();
    }
    public void getChangePasswordResponse(JSONObject jsonObject, OnChangePasswordResponseFinishedListener listener);
}
