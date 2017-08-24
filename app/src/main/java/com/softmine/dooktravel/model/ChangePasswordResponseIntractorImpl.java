package com.softmine.dooktravel.model;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.softmine.dooktravel.util.C;

import org.json.JSONObject;

/**
 * Created by GAURAV on 8/23/2017.
 */

public class ChangePasswordResponseIntractorImpl implements IChangePasswordResponseIntractor {
    @Override
    public void getChangePasswordResponse(JSONObject jsonObject, final OnChangePasswordResponseFinishedListener listener) {
        try {
//            Log.e("DEBUG","REQUEST="+jsonBody.toString());

            String REGISTER_URL= C.BASE_URL+ C.CHANGE_PASSWORD_METHOD;

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    REGISTER_URL,jsonObject ,new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    //Log.e(TAG, response.toString());
                    listener.onSuccess(response.toString());

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    //   Log.e(TAG, "Error: " + error.getMessage());
                    //  utils.hideDialog();
                    listener.onError(error.toString());


                }

            });

            // Adding request to request queue
            // AppController.getInstance().addToRequestQueue(jsonObjReq);
            RequestQueue requestQueue = Volley.newRequestQueue(listener.getAPPContext());
            requestQueue.add(jsonObjReq);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
