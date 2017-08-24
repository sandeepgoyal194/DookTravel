package com.softmine.dooktravel.model;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by GAURAV on 8/24/2017.
 */

public interface ICountryListIntractor {
    interface OnGetCountryListFinishedListener {
        void onGetCountryListSuccess(String response);
        void onGetCountryListError(String response);
        Context getAPPContext();
    }
    public void getCountryListResponse(JSONObject jsonObject, OnGetCountryListFinishedListener listener);
}
