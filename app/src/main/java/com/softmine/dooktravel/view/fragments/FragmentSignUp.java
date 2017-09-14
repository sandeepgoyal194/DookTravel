package com.softmine.dooktravel.view.fragments;


import android.app.Dialog;
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
import com.softmine.dooktravel.model.RegisterStatus;
import com.softmine.dooktravel.model.ProfileDetail;
import com.softmine.dooktravel.presenter.FragmentSignUpPresenterImpl;
import com.softmine.dooktravel.presenter.IFragmentSignUpPresenter;
import com.softmine.dooktravel.util.C;
import com.softmine.dooktravel.util.SharedPreference;
import com.softmine.dooktravel.util.Utils;
import com.softmine.dooktravel.validations.ValidateEditText;
import com.softmine.dooktravel.validations.Validations;
import com.softmine.dooktravel.view.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSignUp extends Fragment implements IFragmentView {

    TextView tvLogin,tvAlreadyAccount;
    Button btnSignUp,btnFacebook;
    ValidateEditText etUserName,edPhone;
    int flags;
    String email="",socailId="",phone="";
    LoginButton loginButton;
    CallbackManager callbackManager;
    Validations validation ;
    Utils utils;
    IFragmentSignUpPresenter mIFragmentSignUpPresenter;
    public FragmentSignUp() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity());

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        validation = new Validations();
        utils  = new Utils();
        tvLogin=(TextView)view.findViewById(R.id.tv_login);
        tvLogin.setOnClickListener(tvLoginCLickListner);
        tvAlreadyAccount=(TextView)view.findViewById(R.id.tv_already_have_account);
        loginButton=(LoginButton)view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        btnSignUp=(Button)view.findViewById(R.id.btnSignUp);
        btnFacebook=(Button)view.findViewById(R.id.btnfb);
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(utils.isInternetOn(getActivity())) {
                    loginButton.performClick();
                }
                else {
                    getDailogConfirm(getString(R.string.internet_issue)
                            , "Internet Issue");
                }
            }
        });
        btnSignUp.setOnClickListener(mSignUpClickLisnter);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        flags = flags | Validations.TYPE_EMAIL;
        etUserName=new ValidateEditText((EditText)view.findViewById(R.id.edEmail),getActivity(),flags);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        flags = flags | Validations.TYPE_MOBILE;
        edPhone=new ValidateEditText((EditText)view.findViewById(R.id.edPhone),getActivity(),flags);
        validation.addtoList(etUserName);
        validation.addtoList(edPhone);

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
        mIFragmentSignUpPresenter=new FragmentSignUpPresenterImpl(this,getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIFragmentSignUpPresenter.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    public void requestData(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                JSONObject json = response.getJSONObject();
                try {
                    if (json != null) {
                        Log.e("DEBUG","DATA=="+json.toString());
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
    void userFacebookLogin(String token,String email){
        JSONObject jsonBody = new JSONObject();
        try {
            this.email=email;
            socailId=token;
            jsonBody.put(C.EMAIL, email);
            jsonBody.put(C.SOCAIL_ID,token);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("DEBUG","Request="+jsonBody.toString());
        mIFragmentSignUpPresenter.validateSignUp(jsonBody);
    }
    View.OnClickListener mSignUpClickLisnter=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(validation.validateAllEditText()) {
                if (utils.isInternetOn(getActivity())) {
                    JSONObject jsonBody = new JSONObject();
                    try {
                        email = etUserName.getEditText().getText().toString();
                        phone=edPhone.getEditText().getText().toString();
                        socailId = "";
                        jsonBody.put(C.EMAIL, email);
                        jsonBody.put(C.SOCAIL_ID, "");
                        jsonBody.put(C.PHONE, phone);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                   mIFragmentSignUpPresenter.validateSignUp(jsonBody);
                }
                else {
                    getDailogConfirm(getString(R.string.internet_issue)
                            , "Internet Issue");
                }
            }

           // ((MainActivity) getActivity()).fragmnetLoader(C.FRAGMENT_OTP, null);

        }
    };
    View.OnClickListener tvLoginCLickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           // ((MainActivity)getActivity()).fragmnetLoader(C.FRAGMENT_SIGNUP,null);
            getActivity().onBackPressed();
        }
    };



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

    @Override
    public void getResponseSuccess(String response) {
        try {
            Log.e(FragmentSignUp.class.getName(), "RESPONSE==" + response);
            Gson gson = new Gson();
            RegisterStatus registerStatus = gson.fromJson(response, RegisterStatus.class);
            if (!registerStatus.getError()) {
           /* Intent intent=new Intent(getActivity(), ActivityHome.class);
            startActivity(intent);*/
                ProfileDetail profileDetail = new ProfileDetail();
                profileDetail.setEmail(email);
                profileDetail.setPhone(phone);
                profileDetail.setSocialid(socailId);
                Bundle bundle = new Bundle();
                bundle.putSerializable(C.DATA, profileDetail);
                bundle.putBoolean(C.IS_SIGNUP, true);
                SharedPreference.getInstance(getActivity()).setString(C.OTP,registerStatus.getOTP());
                ((MainActivity) getActivity()).fragmnetLoader(C.FRAGMENT_OTP, bundle);
            } else {
                getDailogConfirm(registerStatus.getMessage(), "");
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
        utils.showDialog(C.MSG,getActivity());
    }

    @Override
    public void hideProgress() {
        utils.hideDialog();
    }
}
