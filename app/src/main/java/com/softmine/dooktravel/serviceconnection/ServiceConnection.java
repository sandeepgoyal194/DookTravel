package com.softmine.dooktravel.serviceconnection;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.softmine.dooktravel.util.C;
import com.softmine.dooktravel.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by GAURAV on 7/8/2017.
 */

public class ServiceConnection {

    Utils utils=new Utils();
    public ServiceConnection() {
    }

    public void sendToServer(String method, final Map hashMap, final CompleteListener completeListener){
        utils.showDialog(C.MSG,completeListener.getApplicationsContext());
            String REGISTER_URL= C.BASE_URL+method;
        Log.e("url==",REGISTER_URL);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            utils.hideDialog();
                            completeListener.done(response);
                            Toast.makeText(completeListener.getApplicationsContext(),response,Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            utils.hideDialog();
                            Log.e("Error",error.toString());
                            Toast.makeText(completeListener.getApplicationsContext(),error.toString(),Toast.LENGTH_LONG).show();
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                  //  Map<String,String> params = new HashMap<String, String>();

                    return hashMap;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(completeListener.getApplicationsContext());
            requestQueue.add(stringRequest);
        }


        public void getResponse(final CompleteListener completeListener){
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(completeListener.getApplicationsContext());
                String URL = "http://www.kenlabs.co/dook/apis/?device=phone&api=login";
                JSONObject jsonBody = new JSONObject();
                jsonBody.put(C.EMAIL, "pradeep.bansal@techmobia.com");
                jsonBody.put(C.PASSWORD, "abc123");

                jsonBody.put("socialid", "");
                final String mRequestBody = jsonBody.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_VOLLEY", response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_VOLLEY", error.toString());
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                            return null;
                        }
                    }

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseString = "";
                        if (response != null) {

                            responseString = String.valueOf(response.statusCode);

                        }
                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };

                requestQueue.add(stringRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    public void makeJsonObjectRequest(final  String method,JSONObject jsonBody, final CompleteListener completeListener) {

        try {
//            Log.e("DEBUG","REQUEST="+jsonBody.toString());
            utils.showDialog(C.MSG,completeListener.getApplicationsContext());
            String REGISTER_URL= C.BASE_URL+method;

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    REGISTER_URL,jsonBody,new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    utils.hideDialog();

                    Log.e(TAG, response.toString());
                    completeListener.done(response.toString());

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error: " + error.getMessage());
                    utils.hideDialog();

                }

            });

            // Adding request to request queue
           // AppController.getInstance().addToRequestQueue(jsonObjReq);
            RequestQueue requestQueue = Volley.newRequestQueue(completeListener.getApplicationsContext());
            requestQueue.add(jsonObjReq);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
