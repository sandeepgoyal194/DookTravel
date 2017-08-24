package com.softmine.dooktravel.model;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by GAURAV on 8/25/2017.
 */

public interface IProfileListIntractor {
    interface OnGetProfileListFinishedListener {
        void onSuccess(String response);
        void onError(String response);
        Context getAPPContext();
    }
    public void getProfileListResponse(JSONObject jsonObject, OnGetProfileListFinishedListener listener);
}
