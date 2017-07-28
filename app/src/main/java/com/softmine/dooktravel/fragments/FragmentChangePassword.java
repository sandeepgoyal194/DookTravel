package com.softmine.dooktravel.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softmine.dooktravel.R;
import com.softmine.dooktravel.serviceconnection.CompleteListener;
import com.softmine.dooktravel.util.Utils;
import com.softmine.dooktravel.validations.ValidateEditText;
import com.softmine.dooktravel.validations.Validations;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentChangePassword extends Fragment implements CompleteListener {

    int flags;
    ValidateEditText etPassword,etConfirmPassword;
    Button btnSubmit;
    TextView tvForgotPassword;
    Validations validation = new Validations();
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
                        //TODO hit API

                        Utils.showToast(getActivity(),"Under development");
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
    }

    @Override
    public Context getApplicationsContext() {
        return getActivity();
    }
}
