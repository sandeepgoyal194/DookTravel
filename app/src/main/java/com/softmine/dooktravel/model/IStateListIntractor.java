package com.softmine.dooktravel.model;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by GAURAV on 8/24/2017.
 */

public interface IStateListIntractor {
    interface OnGetStateListFinishedListener {
        void onGetStateListSuccess(String response);
        void onGetStateListError(String response);
        Context getAPPContext();

    }
    public void getStateListResponse(JSONObject jsonObject, OnGetStateListFinishedListener listener);
}
