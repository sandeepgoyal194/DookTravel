package com.softmine.dooktravel.model;

import com.android.volley.DefaultRetryPolicy;
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

public class CountryListIntractorImpl implements ICountryListIntractor{
    @Override
    public void getCountryListResponse(JSONObject jsonObject, final OnGetCountryListFinishedListener listener) {
        try {
//            Log.e("DEBUG","REQUEST="+jsonBody.toString());

            String REGISTER_URL= C.BASE_URL+ C.COUNTRY_METHOD;

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    REGISTER_URL,jsonObject ,new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    //Log.e(TAG, response.toString());
                    listener.onGetCountryListSuccess(response.toString());

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    //   Log.e(TAG, "Error: " + error.getMessage());
                    //  utils.hideDialog();
                    listener.onGetCountryListError(error.toString());


                }

            });

            // Adding request to request queue
            // AppController.getInstance().addToRequestQueue(jsonObjReq);
           /* RequestQueue requestQueue = Volley.newRequestQueue(listener.getAPPContext());
            requestQueue.add(jsonObjReq);*/

            RequestQueue requestQueue = Volley.newRequestQueue(listener.getAPPContext());
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    12000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjReq);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
