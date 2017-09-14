package com.softmine.dooktravel.view.fragments;


import android.app.Dialog;
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
import com.softmine.dooktravel.model.RegisterStatus;
import com.softmine.dooktravel.presenter.FragmentChangePasswordPresenterImpl;
import com.softmine.dooktravel.presenter.IFragmentChangePasswordPresenter;
import com.softmine.dooktravel.util.C;
import com.softmine.dooktravel.util.SharedPreference;
import com.softmine.dooktravel.util.Utils;
import com.softmine.dooktravel.validations.ValidateEditText;
import com.softmine.dooktravel.validations.Validations;
import com.softmine.dooktravel.view.ActivityHome;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentChangePassword extends Fragment implements IFragmentView {

    int flags;
    ValidateEditText etPassword,etConfirmPassword;
    Button btnSubmit;
    Validations validation;
    Utils utils;
    IFragmentChangePasswordPresenter mIFragmentChangePasswordPresenter;
    public FragmentChangePassword() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        ActivityHome.tvEdit.setVisibility(View.GONE);

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
        btnSubmit.setOnClickListener(mSubmitClickListner);
        validation.addtoList(etConfirmPassword);
        validation.addtoList(etPassword);
        mIFragmentChangePasswordPresenter=new FragmentChangePasswordPresenterImpl(this,getActivity());
    }

    View.OnClickListener mSubmitClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                if(validation.validateAllEditText()){
                    if(isValid()){
                        if(utils.isInternetOn(getActivity())) {
                            if(SharedPreference.getInstance(getActivity()).getBoolean(C.IS_LOGIN)) {
                                JSONObject jsonBody = new JSONObject();
                                try {
                                    jsonBody.put(C.MEMBER_ID, SharedPreference.getInstance(getActivity()).getString(C.MEMBER_ID));
                                    jsonBody.put(C.TOKEN, SharedPreference.getInstance(getActivity()).getString(C.TOKEN));
                                    jsonBody.put(C.PASSWORD,etPassword.getEditText().getText().toString());
                                    mIFragmentChangePasswordPresenter.validateChangePassword(jsonBody);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
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
    public void onDestroy() {
        super.onDestroy();
        mIFragmentChangePasswordPresenter.onDestroy();
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

    @Override
    public void getResponseSuccess(String response) {
        try {
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
