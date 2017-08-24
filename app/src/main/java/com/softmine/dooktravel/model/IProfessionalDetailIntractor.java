package com.softmine.dooktravel.model;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by GAURAV on 8/24/2017.
 */

public interface IProfessionalDetailIntractor {

    interface OnDetailRegisterFinishedListener {
        void onSuccess(String response);
        void onError(String response);
        Context getAPPContext();
    }
    public void registerUserDetail(JSONObject jsonObject, OnDetailRegisterFinishedListener listener);
    public void updateUserDetail(JSONObject jsonObject, OnDetailRegisterFinishedListener listener);

}
