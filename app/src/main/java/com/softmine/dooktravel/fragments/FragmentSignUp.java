package com.softmine.dooktravel.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.softmine.dooktravel.ActivityHome;
import com.softmine.dooktravel.MainActivity;
import com.softmine.dooktravel.R;
import com.softmine.dooktravel.pojos.LoginStatus;
import com.softmine.dooktravel.pojos.RegisterStatus;
import com.softmine.dooktravel.serviceconnection.CompleteListener;
import com.softmine.dooktravel.serviceconnection.ServiceConnection;
import com.softmine.dooktravel.util.C;
import com.softmine.dooktravel.util.Utils;
import com.softmine.dooktravel.validations.ValidateEditText;
import com.softmine.dooktravel.validations.Validations;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSignUp extends Fragment implements CompleteListener {

    TextView tvLogin,tvAlreadyAccount;
    Button btnSignUp,btnFacebook;
    ValidateEditText etUserName;
    int flags;
    Validations validation = new Validations();
    public FragmentSignUp() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvLogin=(TextView)view.findViewById(R.id.tv_login);
        tvLogin.setOnClickListener(tvLoginCLickListner);
        tvAlreadyAccount=(TextView)view.findViewById(R.id.tv_already_have_account);

        btnSignUp=(Button)view.findViewById(R.id.btnSignUp);
        btnFacebook=(Button)view.findViewById(R.id.btnfb);
        btnSignUp.setOnClickListener(mSignUpClickLisnter);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        flags = flags | Validations.TYPE_EMAIL;
        etUserName=new ValidateEditText((EditText)view.findViewById(R.id.edEmail),getActivity(),flags);


        tvLogin.setTypeface(Utils.getSemiBoldTypeFace(getActivity()));
        tvAlreadyAccount.setTypeface(Utils.getRegularItalicTypeFace(getActivity()));

        btnSignUp.setTypeface(Utils.getSemiBoldTypeFace(getActivity()));

        btnFacebook.setTypeface(Utils.getSemiBoldTypeFace(getActivity()));

        etUserName.getEditText().setTypeface(Utils.getRegularTypeFace(getActivity()));
        validation.addtoList(etUserName);

    }

    View.OnClickListener mSignUpClickLisnter=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
         /*   Map<String,String> params = new HashMap<String, String>();
            params.put(C.EMAIL,etUserName.getText().toString());
            params.put(C.SOCAIL_ID,"");
            ServiceConnection serviceConnection=new ServiceConnection();
            serviceConnection.sendToServer(C.SIGNUP_METHOD,params,FragmentSignUp.this);*/

            if(validation.validateAllEditText()) {

                JSONObject jsonBody = new JSONObject();
                try {
             /*   jsonBody.put(C.EMAIL, "pradeep.bansal@techmobia.com");
                jsonBody.put(C.PASSWORD, "abc123");
                jsonBody.put(C.SOCAIL_ID, "");*/
                    jsonBody.put(C.EMAIL, etUserName.getString());
                    jsonBody.put(C.SOCAIL_ID, "");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ServiceConnection serviceConnection = new ServiceConnection();
                serviceConnection.makeJsonObjectRequest(C.SIGNUP_METHOD, jsonBody, FragmentSignUp.this);
            }
           // serviceConnection.getResponse(FragmentSignUp.this);
        }
    };
    View.OnClickListener tvLoginCLickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           // ((MainActivity)getActivity()).fragmnetLoader(C.FRAGMENT_SIGNUP,null);
            getActivity().onBackPressed();
        }
    };

    @Override
    public void done(String response) {
        Log.e(FragmentSignUp.class.getName(),"RESPONSE=="+response);
        Gson gson = new Gson();
        RegisterStatus registerStatus= gson.fromJson(response,RegisterStatus.class);
        if(!registerStatus.getError()){
           /* Intent intent=new Intent(getActivity(), ActivityHome.class);
            startActivity(intent);*/
           ((MainActivity)getActivity()).fragmnetLoader(C.FRAGMENT_BASIC_DETAIL,null);
        }
        else {
            getDailogConfirm(registerStatus.getMessage(),"");
        }
    }

    @Override
    public Context getApplicationsContext() {
        return getActivity();
    }


    void getDailogConfirm(String dataText, String titleText) {
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
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
