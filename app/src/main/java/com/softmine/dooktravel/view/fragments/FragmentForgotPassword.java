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
import com.softmine.dooktravel.presenter.FragmentForgotPasswordPresenterImpl;
import com.softmine.dooktravel.presenter.IFragmentForgotPasswordPresenter;
import com.softmine.dooktravel.util.C;
import com.softmine.dooktravel.util.Utils;
import com.softmine.dooktravel.validations.ValidateEditText;
import com.softmine.dooktravel.validations.Validations;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentForgotPassword extends Fragment implements IFragmentView{
    int flags;
    ValidateEditText etEmail;
    Button btnSubmit;
    TextView tvForgotPassword;
    Validations validation;
    Utils utils;
    IFragmentForgotPasswordPresenter mIFragmentForgotPasswordPresenter;
    public FragmentForgotPassword() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        validation = new Validations();
        utils=new Utils();
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        etEmail=new ValidateEditText((EditText)view.findViewById(R.id.edEmail),getActivity(),flags);
        tvForgotPassword=(TextView)view.findViewById(R.id.tvForgotPassword) ;
        btnSubmit=(Button)view.findViewById(R.id.btnSubmit);

        etEmail.getEditText().setTypeface(Utils.getRegularTypeFace(getActivity()));
        btnSubmit.setTypeface(Utils.getSemiBoldTypeFace(getActivity()));
        btnSubmit.setOnClickListener(mSubmitClickListner);
        validation.addtoList(etEmail);
        mIFragmentForgotPasswordPresenter=new FragmentForgotPasswordPresenterImpl(this,getActivity());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mIFragmentForgotPasswordPresenter.onDestroy();
    }
    View.OnClickListener mSubmitClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(validation.validateAllEditText()) {
                if (utils.isInternetOn(getActivity())) {
                    JSONObject jsonBody = new JSONObject();
                    try {
                        jsonBody.put(C.EMAIL_FORGOT_PASSWORD, etEmail.getEditText().getText().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mIFragmentForgotPasswordPresenter.validateForgotPassword(jsonBody);
                }
                else {
                    getDailogConfirm(getString(R.string.internet_issue)
                            , "Internet Issue");
                }
            }
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

    void getDailog(String dataText, final String titleText) {
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
                    if(titleText.equals("1")) {
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
            Log.e(FragmentForgotPassword.class.getName(),"RESPONSE=="+response);
            Gson gson = new Gson();
            RegisterStatus registerStatus= gson.fromJson(response,RegisterStatus.class);
            if(!registerStatus.getError()){
                getDailog(registerStatus.getMessage(),"1");
            }
            else {
                getDailog(registerStatus.getMessage(),"");
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
