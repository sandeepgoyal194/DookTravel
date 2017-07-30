package com.softmine.dooktravel.fragments;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.softmine.dooktravel.R;
import com.softmine.dooktravel.pojos.RegisterStatus;
import com.softmine.dooktravel.serviceconnection.CompleteListener;
import com.softmine.dooktravel.serviceconnection.ServiceConnection;
import com.softmine.dooktravel.util.C;
import com.softmine.dooktravel.util.SharedPreference;
import com.softmine.dooktravel.util.Utils;
import com.softmine.dooktravel.validations.ValidateEditText;
import com.softmine.dooktravel.validations.Validations;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentChangePassword extends Fragment implements CompleteListener {

    int flags;
    ValidateEditText etPassword,etConfirmPassword;
    Button btnSubmit;
    Validations validation;
    Utils utils;
    public FragmentChangePassword() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        validation = new Validations();
        utils=new Utils();
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        etPassword=new ValidateEditText((EditText)view.findViewById(R.id.edPassword),getActivity(),flags);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        etConfirmPassword=new ValidateEditText((EditText)view.findViewById(R.id.edConfirmPassword),getActivity(),flags);
        btnSubmit=(Button)view.findViewById(R.id.btnSubmit);


        etPassword.getEditText().setTypeface(Utils.getRegularTypeFace(getActivity()));
        etConfirmPassword.getEditText().setTypeface(Utils.getRegularTypeFace(getActivity()));
        btnSubmit.setTypeface(Utils.getSemiBoldTypeFace(getActivity()));
        btnSubmit.setOnClickListener(mSubmitClickListner);
        validation.addtoList(etConfirmPassword);
        validation.addtoList(etPassword);
    }

    View.OnClickListener mSubmitClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                if(validation.validateAllEditText()){
                    if(isValid()){
                        if(utils.isInternetOn(getActivity())) {
                            //TODO hit API

                            if(SharedPreference.getInstance(getActivity()).getBoolean(C.IS_LOGIN)) {
                                JSONObject jsonBody = new JSONObject();
                                try {
                                    jsonBody.put(C.MEMBER_ID, SharedPreference.getInstance(getActivity()).getString(C.MEMBER_ID));
                                    jsonBody.put(C.TOKEN, SharedPreference.getInstance(getActivity()).getString(C.TOKEN));
                                    jsonBody.put(C.PASSWORD,etPassword.getEditText().getText().toString());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ServiceConnection serviceConnection = new ServiceConnection();
                                serviceConnection.makeJsonObjectRequest(C.CHANGE_PASSWORD_METHOD, jsonBody, FragmentChangePassword.this);
                            }

                          //  Utils.showToast(getActivity(), "Under development");
                        }
                        else {
                            getDailogConfirm(getString(R.string.internet_issue)
                                    , "Internet Issue");
                        }
                    }
                }
        }
    };

    boolean isValid(){
        if( !etPassword.getEditText().getText().toString().equals(etConfirmPassword.getEditText().getText().toString())){
            etConfirmPassword.getEditText().setError(getString(R.string.password_validate));
            return false;
        }
        return true;
    }

    @Override
    public void done(String response) {
        // TODO handle response
        Log.e(FragmentChangePassword.class.getName(),"RESPONSE=="+response);
        Gson gson = new Gson();
        RegisterStatus registerStatus= gson.fromJson(response,RegisterStatus.class);
        if(!registerStatus.getError()){

            getDailogConfirm(registerStatus.getMessage(),"1");
        }
        else {
            getDailogConfirm(registerStatus.getMessage(),"");
        }

    }

    @Override
    public Context getApplicationsContext() {
        return getActivity();
    }




    void getDailogConfirm(String dataText, final String titleText) {
        try {
            final Dialog dialog = new Dialog(getActivity());
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //tell the Dialog to use the dialog.xml as it's layout description
            dialog.setContentView(R.layout.dialog_with_two_button);
            // dialog.setTitle("Android Custom Dialog Box");
            dialog.setCancelable(false);
            TextView dataTextTv = (TextView) dialog.findViewById(R.id.dialog_data_text);
            TextView titleTextTv = (TextView) dialog.findViewById(R.id.dialog_title_text);
            TextView cancelTv = (TextView) dialog.findViewById(R.id.dialog_cancel_text);
            TextView okTextTv = (TextView) dialog.findViewById(R.id.dialog_ok_text);

            cancelTv.setVisibility(View.GONE);
            dataTextTv.setText(dataText);
            titleTextTv.setText(titleText);

            cancelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            okTextTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (titleText.equals("1")) {
                        getActivity().onBackPressed();
                    }

                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
