package com.softmine.dooktravel.validations;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

/**
 * Created by gaurav.garg on 13-10-2015.
 */
public class ValidateEditText {
    private EditText mEditTextView;
    TextInputLayout mTextInputLayoutype;
    private String mEditTextype;
    private Context mContext;
    int mProperties;
    public static int state = 0;
    boolean error = false;
    public ValidateEditText(EditText view, TextInputLayout txtInputLayout, Context context, int properties) {
        mEditTextView = view;
        mTextInputLayoutype=txtInputLayout;
        //mEditTextype = type;
        // TODO Auto-generated constructor stub
        mContext = context ;
        mProperties = properties;

    }
    public ValidateEditText(EditText view, Context context, int properties) {
        mEditTextView = view;
        //mEditTextype = type;
        // TODO Auto-generated constructor stub
        mContext = context ;
        mProperties = properties;

    }

    public int getProperty() {
        return mProperties;
    }
    public String getEditTextype() {
        return mEditTextype;
    }
    public Context getContext() {
        return mContext;
    }


    public String getString() {
        return mEditTextView.getText().toString().trim();
    }
    public void getError(){
        mEditTextView.setError("Required field");
    }
    public void getErrorForEmail(){
        mEditTextView.setError("That doesn’t look like a valid email address – can you check it again please?");
    }
    public void getErrorForMobileNumber(){
        mEditTextView.setError("Number Should be of 10 digit and must start with 7, 8 or 9");
    }

    public void getErrorForPassword(){
        mEditTextView.setError("Password must be alphanumeric with at least 1 special , 1 lower, 1 upper and password length b/w 8 to 16 character.");
    }
    public void getFocus(){
        mEditTextView.requestFocus();
    }
    public void clearFocus(){
        mEditTextView.clearFocus();

    }
    public EditText getEditText(){
        return mEditTextView;
    }
    public TextInputLayout getTextInputLayout(){
        return mTextInputLayoutype;
    }


}

