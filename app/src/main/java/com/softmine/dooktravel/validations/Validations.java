package com.softmine.dooktravel.validations;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gaurav.garg on 13-10-2015.
 */
public class Validations {
    ArrayList<ValidateEditText> mEditTextList = new ArrayList<ValidateEditText>();
    public static final int TYPE_MOBILE = 0x00000002;
    public static final int FLAG_NOT_EMPTY = 0x00000001;
    public static final int TYPE_EMAIL = 0x00000004;
    public static final int TYPE_PINCODE = 0x00000008;
    public static final int TYPE_CITY= 0x00000010;
    public static final int TYPE_State=0x00000020;
    public static final int TYPE_Password=0x00000040;
    static final String regex_Email_pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    static final String regex_MobileNo = "^[7-9][0-9]{9}$";
    static final String regex_PinCode="^([0-9]{6})$";
    static final String regex_Text_Only = "^[a-zA-Z]+$";
    static final String regex_Password = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,16}";//"^[a-zA-Z]\\w{3,14}$";
    static Pattern pattern = null;
    static Matcher match = null;
    boolean isValidationFalse;

    public void addtoList(ValidateEditText editText) {
        mEditTextList.add(editText);
    }
    public void deletetoList(ValidateEditText editText) {
        if(mEditTextList.contains(editText))
            mEditTextList.remove(editText);
    }

    public static boolean isNotEmpty(String text) {
        if (text.length() > 0) {
            return true;
        }
        return false;
    }
    public static boolean isPincode(String text){
        pattern = Pattern.compile(regex_PinCode);
        match=pattern.matcher(text);
        if(match.matches()){
            return true;
        }
        else{
            return false;
        }
    }
    public static boolean isMobileNo(String text) {
        pattern = Pattern.compile(regex_MobileNo);
        match = pattern.matcher(text);
        if (match.matches()) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean isText(String text) {
        pattern = Pattern.compile(regex_Text_Only);
        match = pattern.matcher(text);
        if (match.matches()) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean isPassword(String text) {
        pattern = Pattern.compile(regex_Password);
        match = pattern.matcher(text);
        if (match.matches()) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean isEmailText(String text) {
        pattern = Pattern.compile(regex_Email_pattern);
        match = pattern.matcher(text);
        if (match.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validateAllEditText() {
        isValidationFalse=true;
        for (int i = 0; i < mEditTextList.size(); i++) {
            ValidateEditText editText = mEditTextList.get(i);
            validationOfEditText(editText);

        }
       return isValidationFalse;
    }
    public void validationOfEditText(ValidateEditText editText) {
        if ((editText.getProperty() & FLAG_NOT_EMPTY) != 0) {
            if (!isNotEmpty(editText.getString())) {
                editText.getFocus();
              /*  Toast.makeText(
                        editText.getContext(),
                        "Fill data, it can't be empty", Toast.LENGTH_LONG).show();*/
                editText.getError();
              //  return false;
                isValidationFalse=false;
            }
        }
        if ((editText.getProperty() & TYPE_EMAIL) != 0) {
            if (isNotEmpty(editText.getString())) {
                if (!isEmailText(editText.getString())) {

                    editText.getErrorForEmail();
                    editText.getFocus();
                    //  return false;
                    isValidationFalse = false;
                }
            }
        }
     /*   if ((editText.getProperty() & TYPE_CITY) != 0) {
            if (!isText(editText.getString())) {
                Toast.makeText(editText.getContext(),
                        "Only Text Should be Use For City", Toast.LENGTH_LONG)
                        .show();
                editText.getFocus();
                return false;
            }
        }
        if ((editText.getProperty() & TYPE_State) != 0) {
            if (!isText(editText.getString())) {
                Toast.makeText(editText.getContext(),
                        "Only Text Should be Use For State", Toast.LENGTH_LONG)
                        .show();
                editText.getFocus();
                return false;
            }
        }
        if((editText.getProperty() & TYPE_PINCODE) != 0){
            if(!isPincode(editText.getString())){
                Toast.makeText(editText.getContext(),"Entered Pincode is not Valid",Toast.LENGTH_LONG).show();
                editText.getFocus();
                return false;
            }
        }
        if ((editText.getProperty() & TYPE_Password) != 0) {
            if (!isPassword(editText.getString())) {
                Toast.makeText(
                        editText.getContext(),
                        "Entered Password is not Valid Password  /n Password must be between 4 and 8 alphanumeric only ",
                        Toast.LENGTH_LONG).show();
                editText.getFocus();
                return false;
            }
        }*/
        if ((editText.getProperty() & TYPE_MOBILE) != 0) {
            if (isNotEmpty(editText.getString())) {
                if (!isMobileNo(editText.getString())) {
                    editText.getErrorForMobileNumber();
                    editText.getFocus();
                    //  return false;
                    isValidationFalse = false;
                }
            }
        }

        if ((editText.getProperty() & TYPE_Password) != 0) {
            if (isNotEmpty(editText.getString())) {
                if (!isPassword(editText.getString())) {
                    editText.getErrorForPassword();
                    editText.getFocus();
                    //  return false;
                    isValidationFalse = false;
                }
            }
        }
        //return true;
    }

}
