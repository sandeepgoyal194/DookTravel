package com.softmine.dooktravel.model;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by GAURAV on 8/24/2017.
 */

public interface ICategoryListIntractor {
    interface OnGetCategoryListFinishedListener {
        void onGetCategoryListSuccess(String response);
        void onGetCategoryListError(String response);
        Context getAPPContext();

    }
    public void getCategoryListResponse(JSONObject jsonObject, OnGetCategoryListFinishedListener listener);
}
