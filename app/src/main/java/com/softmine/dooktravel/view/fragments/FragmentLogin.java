package com.softmine.dooktravel.view.fragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.softmine.dooktravel.R;
import com.softmine.dooktravel.model.LoginStatus;
import com.softmine.dooktravel.model.ProfileDetail;
import com.softmine.dooktravel.presenter.FragmentLoginPresenterImpl;
import com.softmine.dooktravel.presenter.IFragmentLoginPresenter;
import com.softmine.dooktravel.util.C;
import com.softmine.dooktravel.util.SharedPreference;
import com.softmine.dooktravel.util.Utils;
import com.softmine.dooktravel.validations.ValidateEditText;
import com.softmine.dooktravel.validations.Validations;
import com.softmine.dooktravel.view.ActivityHome;
import com.softmine.dooktravel.view.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLogin extends Fragment implements IFragmentView {
    int flags;
    Validations validation ;
    TextView tvSignUp,tvforgotPassword,tvDontHaveAccount;
    Button btnLogin,btnFacebook;
    LoginButton loginButton;
    IFragmentLoginPresenter mPresenter;
    CallbackManager callbackManager;
    Utils util;
    boolean mIsFbLogin=false;
    ValidateEditText etUsername,etPassword,etPh;
    public FragmentLogin() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            LoginManager.getInstance().logOut();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        validation = new Validations();
        util=new Utils();
        tvSignUp=(TextView)view.findViewById(R.id.tv_sign_up);
        tvSignUp.setOnClickListener(tvSignUpClickListner);
        btnLogin=(Button)view.findViewById(R.id.btnLogin);
        loginButton=(LoginButton)view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        btnLogin.setOnClickListener(mLoginButtonClickListner);
        tvforgotPassword=(TextView)view.findViewById(R.id.tvForgotPassword);
        tvforgotPassword.setOnClickListener(mForgotPasswordClickListner);
        tvDontHaveAccount=(TextView)view.findViewById(R.id.tv_dont_have_account);
        btnFacebook=(Button)view.findViewById(R.id.btnfb);
            flags = 0 | Validations.FLAG_NOT_EMPTY;
    //    flags = flags | Validations.TYPE_MOBILE;
        etUsername=new ValidateEditText((EditText)view.findViewById(R.id.edUsername),getActivity(),flags);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        etPassword=new ValidateEditText((EditText)view.findViewById(R.id.edPassword),getActivity(),flags);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        etPh=new ValidateEditText((EditText)view.findViewById(R.id.ph),getActivity(),flags);
        final Spannable code = new SpannableString("+");

        code.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0,code.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //etPh.getEditText().setText(code);


        final Spannable code_hint = new SpannableString("+91");

        code_hint.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0,1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        code_hint.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue2)), 2,code_hint.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        etPh.getEditText().setHint(code_hint);
        etPh.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                    if(etPh.getEditText().length()==0) {
                        etPh.getEditText().setText(code);
                    }
                  //  etPh.getEditText().setSelection(etPh.getEditText().getText().toString().length());
                }
                else {
                    if(etPh.getEditText().length()==0 || etPh.getEditText().length()==1) {
                        etPh.getEditText().setText("");
                        etPh.getEditText().setHint(code_hint);
                    }
                }

            }
        });
        etPh.getEditText().addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (s != null && s.length() == 0) {
                    if(etPh.getEditText().hasFocus()) {
                        etPh.getEditText().setText(code);
                        etPh.getEditText().setSelection(etPh.getEditText().getText().toString().length());
                    }
                }
               else if(s!=null && s.length()==4){
                    etUsername.clearFocus();
                    etUsername.getEditText().requestFocus();
                    etUsername.getEditText().setCursorVisible(true);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if(!s.toString().startsWith("+") && s.length()==2){
                    etPh.getEditText().setText("+"+s.toString().substring(0,1));
                }
               else if(!s.toString().startsWith("+") && s.length()==3){
                    etPh.getEditText().setText(s.toString().substring(1,s.toString().length())+s.toString().substring(0,1));
                }
                else if(!s.toString().startsWith("+") && s.length()==4){
                    etPh.getEditText().setText(s.toString().substring(1,s.toString().length())+s.toString().substring(0,1));
                }
                etPh.getEditText().setSelection(etPh.getEditText().getText().toString().length());
            }
        });
        btnFacebook.setOnClickListener(mButtonClickListner);
      //  validation.addtoList(etPassword);
        validation.addtoList(etUsername);
        callbackManager =CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
            Log.e("DEBUG","UserID="+loginResult.getAccessToken().getUserId()+"Token="+loginResult.getAccessToken().getToken());

                requestData();
            }

            @Override
            public void onCancel() {
            Log.e("DEBUG","Login Cancelled");
                Utils.showToast(getActivity(),getString(R.string.login_cancled));
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("DEBUG","error"+error.toString());
            }
        });
        /*final Spannable code = new SpannableString(" + 91");

        code.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 1,2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        code.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue2)), 3,5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        etPh.getEditText().setHint(code);
        etPh.getEditText().setFocusable(false);*/
        mPresenter = new FragmentLoginPresenterImpl(this,getActivity());

    }
    public void requestData(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                JSONObject json = response.getJSONObject();
                try {
                    if (json != null) {
                        Log.e("DEBUG","DATA=="+json.toString());
                    /*    String text = "<b>Name :</b> " + json.getString("name") + "<br><br><b>Email :</b> " + json.getString("email") + "<br><br><b>Profile link :</b> " + json.getString("link");
                    //    details_txt.setText(Html.fromHtml(text));
                   //     profile.setProfileId(json.getString("id"));
                        Log.e("DEBUG","text=="+text+"===id=="+json.getString("id"));*/
                      /*  name=json.getString("name");
                        email=json.getString("email");
                        regId=json.getString("id");*/

                        userFacebookLogin(json.getString("id"),json.getString("email"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showToast(getActivity(),getString(R.string.email_required));
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    View.OnClickListener mForgotPasswordClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((MainActivity)getActivity()).fragmnetLoader(8,null);
        }
    };

    View.OnClickListener mButtonClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(util.isInternetOn(getActivity())) {
                loginButton.performClick();
            }
            else {
                getDailogConfirm(getString(R.string.internet_issue)
                        , "Internet Issue");
            }
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onDestroy() {
        try {
            super.onDestroy();
            mPresenter.onDestroy();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    void userFacebookLogin(String token, String email){
        JSONObject jsonBody = new JSONObject();
        try {
         /*   jsonBody.put(C.EMAIL, "");
            jsonBody.put(C.PASSWORD, "");*/
            jsonBody.put(C.SOCAIL_ID,token);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mIsFbLogin=true;
        Log.e("DEBUG","LOGIN=="+jsonBody);
        mPresenter.validateLoginSocail(jsonBody);

    }
    View.OnClickListener mLoginButtonClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(validation.validateAllEditText()) {
                if(etPh.getEditText().getText()!=null &&etPh.getEditText().getText().toString().length()>1) {

                        if (util.isInternetOn(getActivity())) {
                            JSONObject jsonBody = new JSONObject();
                            try {
                                jsonBody.put(C.ACTION, C.Login);
                                jsonBody.put(C.PHONE, etPh.getEditText().getText().toString().substring(1) + etUsername.getEditText().getText().toString());

                                //   jsonBody.put(C.PASSWORD, etPassword.getEditText().getText().toString());
                                //     jsonBody.put(C.SOCAIL_ID, "");
                                Log.e("DEBUG", "json=" + jsonBody.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mIsFbLogin = false;
                            mPresenter.validateLogin(jsonBody);
                        } else {
                            getDailogConfirm(getString(R.string.internet_issue)
                                    , "Internet Issue");
                        }

                }
                else {
                    Toast.makeText(getActivity(),getString(R.string.enter_country_code),Toast.LENGTH_LONG).show();
                    etPh.getEditText().requestFocus();
                }
            }
            /*Intent intent = new Intent(getActivity(), ActivityHome.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);*/
        }
    };
    View.OnClickListener tvSignUpClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((MainActivity)getActivity()).fragmnetLoader(C.FRAGMENT_SIGNUP,null);
        }
    };



    boolean isAllValid(){
        if(etUsername.getEditText().getText().toString().length()!=10){
            etUsername.getEditText().setError(getString(R.string.valid_phone));
            etUsername.getEditText().requestFocus();
            return false;
        }
        return true;
    }



    @Override
    public void getResponseSuccess(String response) {
        try {
            Log.e(FragmentLogin.class.getName(), "RESPONSE==" + response);
            // Toast.makeText(getActivity(),response,Toast.LENGTH_LONG).show();

            Gson gson = new Gson();
            LoginStatus loginStatus = gson.fromJson(response, LoginStatus.class);
            if (!loginStatus.getError()) {
                SharedPreference.getInstance(getActivity()).setString(C.TOKEN, loginStatus.getMember().getToken());
                SharedPreference.getInstance(getActivity()).setString(C.MEMBER_ID, loginStatus.getMember().getMemberId());
                SharedPreference.getInstance(getActivity()).setString(C.EMAIL, loginStatus.getMember().getEmailId());
                /*Intent intent = new Intent(getActivity(), ActivityHome.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);*/

                ProfileDetail profileDetail = new ProfileDetail();
                profileDetail.setEmail(loginStatus.getMember().getEmailId());
                profileDetail.setPhone(loginStatus.getMember().getPhone());
                Bundle bundle = new Bundle();
                bundle.putSerializable(C.DATA, profileDetail);
                bundle.putBoolean(C.IS_SIGNUP, false);

                if(mIsFbLogin){
                    SharedPreference.getInstance(getActivity()).setBoolean(C.IS_LOGIN, true);

                    Intent intent = new Intent(getActivity(), ActivityHome.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else {
                    SharedPreference.getInstance(getActivity()).setString(C.OTP, loginStatus.getMember().getOtp());
                    ((MainActivity) getActivity()).fragmnetLoader(C.FRAGMENT_OTP, bundle);
                }
            } else {
                getDailogConfirm(loginStatus.getMessage(), "");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void getResponseError(String response) {

    }

    @Override
    public void showProgress() {
        util.showDialog(C.MSG,getActivity());
    }

    @Override
    public void hideProgress() {
        util.hideDialog();
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
