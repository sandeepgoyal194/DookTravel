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
 * Created by GAURAV on 8/25/2017.
 */

public class StateListIntractorImpl implements IStateListIntractor {
    @Override
    public void getStateListResponse(JSONObject jsonObject, final OnGetStateListFinishedListener listener) {
        try {
//            Log.e("DEBUG","REQUEST="+jsonBody.toString());

            String REGISTER_URL= C.BASE_URL+ C.PROFILE_METHOD;

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    REGISTER_URL,jsonObject ,new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    //Log.e(TAG, response.toString());
                    listener.onGetStateListSuccess(response.toString());

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    //   Log.e(TAG, "Error: " + error.getMessage());
                    //  utils.hideDialog();
                    listener.onGetStateListError(error.toString());


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
