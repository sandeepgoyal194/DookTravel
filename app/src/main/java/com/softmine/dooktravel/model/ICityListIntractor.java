package com.softmine.dooktravel.model;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by GAURAV on 8/25/2017.
 */

public interface ICityListIntractor {
    interface OnGetCityListFinishedListener {
        void onGetCityListSuccess(String response);
        void onGetCityListError(String response);
        Context getAPPContext();

    }
    public void getCityListResponse(JSONObject jsonObject, OnGetCityListFinishedListener listener);
}
