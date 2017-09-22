package com.softmine.dooktravel.view.fragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.softmine.dooktravel.model.ProfileDetail;
import com.softmine.dooktravel.model.RegisterStatus;
import com.softmine.dooktravel.presenter.FragmentOtpPresenterImpl;
import com.softmine.dooktravel.presenter.IFragmentOtpPresenter;
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
public class FragmentOtp extends Fragment implements IFragmentView {
    ValidateEditText edit1ED,edit2ED,edit3ED,edit4ED;
    int flags;
    Validations validation ;
    Button btnResendOtp,btnSubmit;
    ProfileDetail profileDtl;
    IFragmentOtpPresenter mPresenter;
    Utils util;
    boolean isSignUp=false;
    StringBuilder str=new StringBuilder();
    public FragmentOtp() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            profileDtl = (ProfileDetail) bundle.getSerializable(C.DATA);
            isSignUp=bundle.getBoolean(C.IS_SIGNUP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_otp, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        validation = new Validations();
        util=new Utils();
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        edit1ED=new ValidateEditText((EditText)view.findViewById(R.id.editText1),getActivity(),flags);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        edit2ED=new ValidateEditText((EditText)view.findViewById(R.id.editText2),getActivity(),flags);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        edit3ED=new ValidateEditText((EditText)view.findViewById(R.id.editText3),getActivity(),flags);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        edit4ED=new ValidateEditText((EditText)view.findViewById(R.id.editText4),getActivity(),flags);
        btnResendOtp=(Button)view.findViewById(R.id.btnResendOtp);
        btnResendOtp.setOnClickListener(mResendOtpClickListner);
        btnSubmit=(Button)view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(mSubmitClickListner);
        validation.addtoList(edit1ED);
        validation.addtoList(edit2ED);
        validation.addtoList(edit3ED);
        validation.addtoList(edit4ED);


        edit1ED.getEditText().addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (edit1ED.getEditText().getText().toString().length() == 1) {

                    edit1ED.clearFocus();
                    edit2ED.getEditText().requestFocus();
                    edit2ED.getEditText().setCursorVisible(true);

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {



            }

            public void afterTextChanged(Editable s) {

            }
        });


        edit2ED.getEditText().addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (edit2ED.getEditText().getText().toString().length() == 1) {
                    edit2ED.getEditText().clearFocus();
                    edit3ED.getEditText().requestFocus();
                    edit3ED.getEditText().setCursorVisible(true);

                }
                if(s.length()==0){
                    edit1ED.getEditText().requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {



            }

            public void afterTextChanged(Editable s) {


            }
        });


        edit3ED.getEditText().addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (edit3ED.getEditText().getText().toString().length() == 1) {
                    edit3ED.getEditText().clearFocus();
                    edit4ED.getEditText().requestFocus();
                    edit4ED.getEditText().setCursorVisible(true);

                }
                if(s.length()==0){
                    edit2ED.getEditText().requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {


            }

            public void afterTextChanged(Editable s) {


            }
        });


        edit4ED.getEditText().addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

                edit4ED.getEditText().clearFocus();
                if(s.length()==0){
                    edit3ED.getEditText().requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {


            }

            public void afterTextChanged(Editable s) {

            }
        });
        mPresenter = new FragmentOtpPresenterImpl(this,getActivity());
    }

    View.OnClickListener mResendOtpClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put(C.PHONE, profileDtl.getPhone());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mPresenter.resend(jsonBody);

        }
    };
    View.OnClickListener mSubmitClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(validation.validateAllEditText()) {

                str.append(edit1ED.getEditText().getText().toString()+
                        edit2ED.getEditText().getText().toString()
                        +edit3ED.getEditText().getText().toString()+edit4ED.getEditText().getText().toString());
                if (SharedPreference.getInstance(getActivity()).getString(C.OTP).equals(str.toString())) {
                    if (isSignUp) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(C.DATA, profileDtl);
                        ((MainActivity) getActivity()).fragmnetLoader(C.FRAGMENT_BASIC_DETAIL, bundle);
                    } else {
                        SharedPreference.getInstance(getActivity()).setBoolean(C.IS_LOGIN, true);

                        Intent intent = new Intent(getActivity(), ActivityHome.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
                else {
                    Utils.showToast(getActivity(),getString(R.string.Otp_wrong));
                }
            }


        }
    };

    @Override
    public void getResponseSuccess(String response) {
        try {
            Log.e(FragmentOtp.class.getName(), "RESPONSE==" + response);
            // Toast.makeText(getActivity(),response,Toast.LENGTH_LONG).show();

            Gson gson = new Gson();
            RegisterStatus registerStatus = gson.fromJson(response, RegisterStatus.class);
            if (!registerStatus.getError()) {
                SharedPreference.getInstance(getActivity()).setString(C.OTP,registerStatus.getOTP());
                Utils.showToast(getActivity(),getString(R.string.otp_send_successfully));
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
