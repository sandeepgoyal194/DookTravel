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
import com.softmine.dooktravel.serviceconnection.CompleteListener;
import com.softmine.dooktravel.serviceconnection.ServiceConnection;
import com.softmine.dooktravel.util.C;
import com.softmine.dooktravel.util.SharedPreference;
import com.softmine.dooktravel.util.Utils;
import com.softmine.dooktravel.validations.ValidateEditText;
import com.softmine.dooktravel.validations.Validations;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLogin extends Fragment implements CompleteListener{
    int flags;
    Validations validation = new Validations();
    TextView tvSignUp,tvforgotPassword,tvDontHaveAccount;
    Button btnLogin,btnFacebook;
    ValidateEditText etUsername,etPassword;
    public FragmentLogin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvSignUp=(TextView)view.findViewById(R.id.tv_sign_up);
        tvSignUp.setOnClickListener(tvSignUpClickListner);
        btnLogin=(Button)view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(mLoginButtonClickListner);
        tvforgotPassword=(TextView)view.findViewById(R.id.tvForgotPassword);
        tvDontHaveAccount=(TextView)view.findViewById(R.id.tv_dont_have_account);
        btnFacebook=(Button)view.findViewById(R.id.btnfb);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        flags = flags | Validations.TYPE_EMAIL;
        etUsername=new ValidateEditText((EditText)view.findViewById(R.id.edUsername),getActivity(),flags);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        etPassword=new ValidateEditText((EditText)view.findViewById(R.id.edPassword),getActivity(),flags);

        etUsername.getEditText().setTypeface(Utils.getRegularTypeFace(getActivity()));
        etPassword.getEditText().setTypeface(Utils.getRegularTypeFace(getActivity()));
        tvSignUp.setTypeface(Utils.getSemiBoldTypeFace(getActivity()));
        btnLogin.setTypeface(Utils.getSemiBoldTypeFace(getActivity()));
        tvforgotPassword.setTypeface(Utils.getRegularTypeFace(getActivity()));
        tvDontHaveAccount.setTypeface(Utils.getRegularItalicTypeFace(getActivity()));
        btnFacebook.setTypeface(Utils.getSemiBoldTypeFace(getActivity()));
        validation.addtoList(etPassword);
        validation.addtoList(etUsername);
    }

    View.OnClickListener mLoginButtonClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*Intent  intent=new Intent(getActivity(), ActivityHome.class);
            startActivity(intent);*/
            if(validation.validateAllEditText()) {
                Map<String, String> params = new HashMap<String, String>();
                params.put(C.EMAIL, etUsername.getString());
                params.put(C.PASSWORD, etUsername.getString());
                params.put(C.SOCAIL_ID, "");

                JSONObject jsonBody = new JSONObject();
                try {
             /*   jsonBody.put(C.EMAIL, "pradeep.bansal@techmobia.com");
                jsonBody.put(C.PASSWORD, "abc123");
                jsonBody.put(C.SOCAIL_ID, "");*/
                    jsonBody.put(C.EMAIL, etUsername.getString());
                    jsonBody.put(C.PASSWORD, etPassword.getString());
                    jsonBody.put(C.SOCAIL_ID, "");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ServiceConnection serviceConnection = new ServiceConnection();
                serviceConnection.makeJsonObjectRequest(C.LOGIN_METHOD, jsonBody, FragmentLogin.this);
            }
        }
    };
    View.OnClickListener tvSignUpClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((MainActivity)getActivity()).fragmnetLoader(C.FRAGMENT_SIGNUP,null);
        }
    };

    @Override
    public void done(String response) {
        Log.e(FragmentLogin.class.getName(),"RESPONSE=="+response);
       // Toast.makeText(getActivity(),response,Toast.LENGTH_LONG).show();

        Gson gson = new Gson();
        LoginStatus loginStatus= gson.fromJson(response,LoginStatus.class);
        if(!loginStatus.getError()){
            SharedPreference.getInstance(getActivity()).setString(C.TOKEN,loginStatus.getMember().getToken());
            SharedPreference.getInstance(getActivity()).setString(C.MEMBER_ID,loginStatus.getMember().getMemberId());
            SharedPreference.getInstance(getActivity()).setString(C.EMAIL,loginStatus.getMember().getEmailId());
            SharedPreference.getInstance(getActivity()).setBoolean(C.IS_LOGIN,true);
            Intent intent=new Intent(getActivity(), ActivityHome.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else{
            getDailogConfirm(loginStatus.getMessage(),"");
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
