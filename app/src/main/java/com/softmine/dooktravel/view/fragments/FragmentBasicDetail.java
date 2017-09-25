package com.softmine.dooktravel.view.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.softmine.dooktravel.R;
import com.softmine.dooktravel.model.LoginStatus;
import com.softmine.dooktravel.model.Profile;
import com.softmine.dooktravel.model.ProfileDetail;
import com.softmine.dooktravel.model.RegisterStatus;
import com.softmine.dooktravel.pojos.CategoryList;
import com.softmine.dooktravel.pojos.CityList;
import com.softmine.dooktravel.pojos.CountryList;
import com.softmine.dooktravel.pojos.ProfileStatus;
import com.softmine.dooktravel.pojos.StateList;
import com.softmine.dooktravel.presenter.FragmentBasicDetailPresenterImpl;
import com.softmine.dooktravel.presenter.FragmentProfessionalDetailPresenterImpl;
import com.softmine.dooktravel.presenter.IFragmentBasicDetailPresenter;
import com.softmine.dooktravel.presenter.IFragmentProfessionalDetailPresenter;
import com.softmine.dooktravel.util.C;
import com.softmine.dooktravel.util.SharedPreference;
import com.softmine.dooktravel.util.Utils;
import com.softmine.dooktravel.validations.ValidateEditText;
import com.softmine.dooktravel.validations.Validations;
import com.softmine.dooktravel.view.ActivityHome;
import com.softmine.dooktravel.view.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBasicDetail extends Fragment implements IFragmentView{
    private DatePickerDialog DatePickerDialogDob,DatePickerDialogWorking;
    TextView tvbasicDetail,tvGender,tvDob,tvMaritalStatus,spnDateOfBirth,
            tvProfesstionalDetail, tvCategory, tvWorkingHour, tvWorkingSince,
            tvCurrentAdd, tvCountry, tvCity, tvProvince, tvAboutMe, tvPostal,tvTextCount;;
    ValidateEditText edFirstName,edMiddleName,edLastName,edPassword,edConfirmPassword,edSkypeID,edPrimary,
            etOraginization, etDesignation, etAddress, etAboutMe, etZipCode,edEmail,edSecondary;

    Spinner spnCategory, spnCountry, spnCity, spnProvince;
    CountryList countryList;
    StateList stateList;
    CityList cityList;
    CategoryList categoryList;
    int mCountryPos = 0, mCityPos = 0, mStatePos = 0, mCategoryPos = 0;
    ArrayAdapter<String> spinnerCategoryAdapter=null;
    ArrayAdapter<String> spinnerStateAdapter=null;
    ArrayAdapter<String> spinnerCountryAdapter=null;

    ArrayAdapter<String> spinnerCityAdapter=null;

    Button btnNext;
    ImageView imgProfile,tvUpload;
   // View viewUploadImage;
    Spinner spnMaritalStatus,spnGender;
    int flags;
    Validations validation ;
    private SimpleDateFormat dateFormatter;
    private int year;
    private int month;
    private int day;
    Utils util;
    Profile profile;
    boolean isEditProfile=false;
    ProfileDetail profileDetail ;
    ProfileDetail profileDtl;
    String action="";
    private int GALLERY = 1, CAMERA = 2;
    private static final String IMAGE_DIRECTORY = "/dooktravel";
    String[] gender = new String[]{
            "Gender",
            "Male",
            "Female"
    };
    String[] matirailStatus = new String[]{
           "Marital Status",
        "Single",
      "Married",
       "Widowed",
        "Separated",
       "Divorced"
    };
    private int PICK_IMAGE_REQUEST = 1;
    IFragmentBasicDetailPresenter mIFragmentBasicDetailPresenter;
    IFragmentProfessionalDetailPresenter mIFragmentProfessionalDetailPresenter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=21){
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        profileDetail = new ProfileDetail();
        if(SharedPreference.getInstance(getActivity()).getBoolean(C.IS_LOGIN)){
            C.isloggedIn=true;
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                profile = (Profile) bundle.getSerializable(C.DATA);
                isEditProfile=bundle.getBoolean(C.IS_EDIT_PROFILE);
            }
        }
        else {
            C.isloggedIn=false;
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                isEditProfile=true;
                profileDtl = (ProfileDetail) bundle.getSerializable(C.DATA);
            }
        }
        mIFragmentBasicDetailPresenter=new FragmentBasicDetailPresenterImpl(this,getActivity());
        mIFragmentProfessionalDetailPresenter=new FragmentProfessionalDetailPresenterImpl(this,getActivity());
    }

    public FragmentBasicDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_basic_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        validation = new Validations();
        util=new Utils();
        dateFormatter = new SimpleDateFormat(C.DATE_FORMAT);
        tvbasicDetail=(TextView)view.findViewById(R.id.tvBasicDetail);
        tvGender=(TextView)view.findViewById(R.id.tvGender);

        tvDob=(TextView)view.findViewById(R.id.tvdateOfBirth);
        tvUpload=(ImageView)view.findViewById(R.id.tvUpload);
        spnDateOfBirth=(TextView)view.findViewById(R.id.spinner_dob);
        spnDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             DatePickerDialogDob.show();
            }
        });
        tvMaritalStatus=(TextView)view.findViewById(R.id.tvMaritalStatus);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        edFirstName=new ValidateEditText((EditText)view.findViewById(R.id.edFirstname),getActivity(),flags);
        edMiddleName=new ValidateEditText((EditText)view.findViewById(R.id.edMiddleName),getActivity(),flags);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        edLastName=new ValidateEditText((EditText)view.findViewById(R.id.edlastName),getActivity(),flags);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        edPassword=new ValidateEditText((EditText)view.findViewById(R.id.edPassword),getActivity(),flags);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        edConfirmPassword=new ValidateEditText((EditText)view.findViewById(R.id.edConfirmPassword),getActivity(),flags);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        edSkypeID=new ValidateEditText((EditText)view.findViewById(R.id.edSkype),getActivity(),flags);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        flags = flags | Validations.TYPE_MOBILE;

        edPrimary=new ValidateEditText((EditText)view.findViewById(R.id.edContact1),getActivity(),flags);


        edSecondary=new ValidateEditText((EditText)view.findViewById(R.id.edContact2),getActivity(),flags);

       /* final Spannable code = new SpannableString("(+91) Primary Number");

        code.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.hint)), 0,1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        code.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.background_blue)), 1,4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        code.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.hint)), 4,code.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        final Spannable code = new SpannableString("(+91) Primary Number");

        code.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.hint)), 0,1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        code.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.background_blue)), 1,4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        code.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.hint)), 4,code.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        final Spannable code1= new SpannableString("(+91) Secondary Number");

        code1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.hint)), 0,1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        code1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.background_blue)), 1,4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        code1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.hint)), 4,code.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        edPrimary.getEditText().setHint(code);*/
        edPrimary.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edPrimary.getEditText().getText().toString().length()>8){
                    getDialPad(Utils.getSubstringPhone(edPrimary.getEditText().getText().toString()));
                }
            }
        });


     //   edSecondary.getEditText().setHint(code1);
        edSecondary.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edSecondary.getEditText().getText().toString().length()>8){
                    getDialPad(Utils.getSubstringPhone(edSecondary.getEditText().getText().toString()));
                }
            }
        });
      /*  edSecondary.getEditText().addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (s != null && s.length() == 5 && (s.charAt(4) < '7')) {
                    edSecondary.getEditText().setText("");
                    edSecondary.getEditText().setError("Number should start with 9,8 and 7");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!s.toString().startsWith("+91-")) {
                    edSecondary.getEditText().setText("+91-");
                    Selection.setSelection( edSecondary.getEditText().getText(),  edSecondary.getEditText()
                            .getText().length());

                }
            }
        });*/

        edEmail=new ValidateEditText((EditText)view.findViewById(R.id.edEmail),getActivity(),flags);

        btnNext=(Button) view.findViewById(R.id.btnNext);
        spnMaritalStatus=(Spinner)view.findViewById(R.id.spinner_marital_status);
        spnGender=(Spinner)view.findViewById(R.id.spinner_gender);
        final List<String> genderList = new ArrayList<>(Arrays.asList(gender));
         List<String> spnMaritalStatusList = new ArrayList<>(Arrays.asList(matirailStatus));


        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(),R.layout.spinner_item_new,genderList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGender.setAdapter(spinnerArrayAdapter);


        final ArrayAdapter<String> spinnerMaritalAdapter = new ArrayAdapter<String>(
                getActivity(),R.layout.spinner_item_new,spnMaritalStatusList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerMaritalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMaritalStatus.setAdapter(spinnerMaritalAdapter);

      //  viewUploadImage=(View)view.findViewById(R.id.rl_image);
     //   viewUploadImage.setOnClickListener(mUploadImageClickListner);
        imgProfile=(ImageView)view.findViewById(R.id.imgProfile);
        imgProfile.setOnClickListener(mUploadImageClickListner);

        btnNext.setOnClickListener(mNextClickListner);
        validation.addtoList(edLastName);
        validation.addtoList(edFirstName);
        validation.addtoList(edPrimary);
        Calendar newCalendar = Calendar.getInstance();
        year  = newCalendar.get(Calendar.YEAR);
        month = newCalendar.get(Calendar.MONTH);
        day   = newCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialogDob = new DatePickerDialog(getActivity(), new android.app.DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int y, int monthOfYear, int dayOfMonth) {
                year  = y;
                month = monthOfYear;
                day   = dayOfMonth;
                Calendar newDate = Calendar.getInstance();
                newDate.set(y, monthOfYear, dayOfMonth);
                spnDateOfBirth.setText(dateFormatter.format(newDate.getTime()));
            }

        },year, month, day);
        DatePickerDialogDob.getDatePicker().setMaxDate(System.currentTimeMillis());




        tvProfesstionalDetail = (TextView) view.findViewById(R.id.tvProfessionalDetail);
        tvCategory = (TextView) view.findViewById(R.id.tvCategory);
        tvWorkingHour = (TextView) view.findViewById(R.id.tvWorkingHour);
        tvCurrentAdd = (TextView) view.findViewById(R.id.tvCurrentAddress);
        tvCountry = (TextView) view.findViewById(R.id.tvCountry);
        tvCity = (TextView) view.findViewById(R.id.tvCity);
        tvProvince = (TextView) view.findViewById(R.id.tvProvince);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        flags = flags | Validations.TYPE_PINCODE;
        etZipCode = new ValidateEditText((EditText) view.findViewById(R.id.et_Postal), getActivity(), flags);
        tvAboutMe = (TextView) view.findViewById(R.id.tvAboutUs);
        tvWorkingSince = (TextView) view.findViewById(R.id.tv_working_since);
        tvWorkingSince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogWorking.show();
            }
        });
        tvPostal = (TextView) view.findViewById(R.id.et_Postal);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        etOraginization = new ValidateEditText((EditText) view.findViewById(R.id.edOrganization), getActivity(), flags);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        etDesignation = new ValidateEditText((EditText) view.findViewById(R.id.edDesignation), getActivity(), flags);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        etAddress = new ValidateEditText((EditText) view.findViewById(R.id.edAddress), getActivity(), flags);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        etAboutMe = new ValidateEditText((EditText) view.findViewById(R.id.edAboutMe), getActivity(), flags);
        tvTextCount = (TextView) view.findViewById(R.id.tvTextCount);
        spnCategory = (Spinner) view.findViewById(R.id.spinner_category);

        spnCategory.setOnItemSelectedListener(mOnCategorySelectedListner);
        spnCountry = (Spinner) view.findViewById(R.id.spinner_country);
        spnCountry.setOnItemSelectedListener(mOnCountrySelectedListner);
        spnCity = (Spinner) view.findViewById(R.id.spinner_city);
        spnProvince = (Spinner) view.findViewById(R.id.spinner_province);
        spnProvince.setOnItemSelectedListener(mOnStateItemSelectedListner);
        spnCity.setOnItemSelectedListener(mOnCityItemSelectedListner);

        etAboutMe.getEditText().addTextChangedListener(mTextChangeListner);

        DatePickerDialogWorking = new DatePickerDialog(getActivity(), new android.app.DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int y, int monthOfYear, int dayOfMonth) {
                year = y;
                month = monthOfYear;
                day = dayOfMonth;
                Calendar newDate = Calendar.getInstance();
                newDate.set(y, monthOfYear, dayOfMonth);
                tvWorkingSince.setText(dateFormatter.format(newDate.getTime()));
            }

        }, year, month, day);
        DatePickerDialogWorking.getDatePicker().setMaxDate(System.currentTimeMillis());

        validation.addtoList(etAboutMe);
        try {

            if(C.isloggedIn) {

                edPassword.getEditText().setVisibility(View.GONE);
                edConfirmPassword.getEditText().setVisibility(View.GONE);
                getProfileDetail();
                if(!isEditProfile){
                    disableViews();
                }
                //showDetails(profile);
            }
            else {
             /*   validation.addtoList(edPassword);
                validation.addtoList(edConfirmPassword);*/

                edEmail.getEditText().setFocusable(false);
                if(profileDtl.getPhone()!=null && profileDtl.getPhone().length()>0){
                    edPrimary.getEditText().setFocusable(false);
                }
                edEmail.getEditText().setText(profileDtl.getEmail());
                edPrimary.getEditText().setText(profileDtl.getPhone());
                getCategoryList();

            }
            if (C.isloggedIn) {
                if (!isEditProfile) {
                    ActivityHome.tvEdit.setVisibility(View.VISIBLE);
                } else {
                    ActivityHome.tvEdit.setVisibility(View.GONE);

                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    AdapterView.OnItemSelectedListener mOnCategorySelectedListner = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (categoryList != null && position != 0 && position != mCategoryPos) {
                //    profileDetail.setStateid(stateList.getState().get(position - 1).getId());
                mCategoryPos = position;
                if (C.isloggedIn) {
                    profile.setCategoryId(categoryList.getCategory().get(position - 1).getCategoryId());
                } else {
                    profileDetail.setCategoryid(categoryList.getCategory().get(position - 1).getCategoryId());
                }

            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener mOnStateItemSelectedListner = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (stateList != null && position != 0 && position != mStatePos) {
                //    profileDetail.setStateid(stateList.getState().get(position - 1).getId());
                mStatePos = position;
                if (C.isloggedIn) {
                    profile.setStateId(stateList.getState().get(position - 1).getId());
                } else {
                    profileDetail.setStateid(stateList.getState().get(position - 1).getId());
                }

                getCityList(stateList.getState().get(position - 1).getId());
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener mOnCountrySelectedListner = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (countryList != null && position != 0 && position != mCountryPos) {
                mCountryPos = position;
                //    profileDetail.setCountryid(countryList.getCountry().get(position -

                if (C.isloggedIn) {
                    profile.setCountryId(countryList.getCountry().get(position - 1).getId());
                } else {
                    profileDetail.setCountryid(countryList.getCountry().get(position - 1).getId());
                }
                getStateList(countryList.getCountry().get(position - 1).getId());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener mOnCityItemSelectedListner = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (cityList != null && position != 0 && position != mCityPos) {
                mCityPos = position;
                if (C.isloggedIn) {
                    profile.setCity(cityList.getCity().get(position - 1).getId());
                } else {
                    profileDetail.setCityid(cityList.getCity().get(position - 1).getId());
                }
                //profileDetail.setCityid(cityList.getCity().get(position-1).getId());
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    void getStateList(String countryId) {
        Log.e("DEBUG", "countryId=" + countryId);

        action = C.STATE_METHOD;

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put(C.COUNTRY_ID, countryId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(C.isloggedIn) {
            mIFragmentProfessionalDetailPresenter.getStateList(jsonBody,false);
        }
        else{
            mIFragmentProfessionalDetailPresenter.getStateList(jsonBody,true);
        }

    }
    void getCategoryList() {
        action = C.CATEGORY_LIST_METHOD;
        if(C.isloggedIn) {
            mIFragmentProfessionalDetailPresenter.getCategoryList(null, false);
        }
        else {
            mIFragmentProfessionalDetailPresenter.getCategoryList(null, true);
        }
    }
    void getCityList(String stateId) {
        Log.e("DEBUG", "StateID=" + stateId);
        action = C.CITY_METHOD;

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put(C.STATE_ID, stateId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(C.isloggedIn) {
            mIFragmentProfessionalDetailPresenter.getCityList(jsonBody,false);
        }
        else{
            mIFragmentProfessionalDetailPresenter.getCityList(jsonBody,true);
        }

    }
    TextWatcher mTextChangeListner=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()<=250)
                tvTextCount.setText("" + s.length() + "/" + "250");

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    void disableViews(){
        tvUpload.setVisibility(View.GONE);
        //viewUploadImage.setClickable(false);
        edFirstName.getEditText().setFocusable(false);
        edMiddleName.getEditText().setFocusable(false);
        edLastName.getEditText().setFocusable(false);
        edPassword.getEditText().setFocusable(false);
        edConfirmPassword.getEditText().setFocusable(false);
        edPrimary.getEditText().setFocusable(false);
        edSecondary.getEditText().setFocusable(false);
        edEmail.getEditText().setFocusable(false);
        edSkypeID.getEditText().setFocusable(false);
        spnGender.setFocusable(false);
        spnGender.setBackgroundResource(R.drawable.spinner_back_gender_u);
        spnGender.setClickable(false);
        spnDateOfBirth.setBackgroundResource(R.drawable.spinner_back_deatils_u);
        spnDateOfBirth.setFocusable(false);
        spnDateOfBirth.setClickable(false);
        spnMaritalStatus.setBackgroundResource(R.drawable.spinner_back_marital_status_u);
        spnMaritalStatus.setFocusable(false);
        spnMaritalStatus.setClickable(false);

        etOraginization.getEditText().setFocusable(false);
        etDesignation.getEditText().setFocusable(false);
        spnCategory.setBackgroundResource(R.drawable.spinner_back_category_u);

        spnCategory.setFocusable(false);
        spnCategory.setClickable(false);
        tvWorkingSince.setBackgroundResource(R.drawable.spinner_back_marital_status_u);
        tvWorkingSince.setFocusable(false);
        tvWorkingSince.setClickable(false);
        spnCountry.setBackgroundResource(R.drawable.spinner_back_country_u);
        spnCountry.setClickable(false);
        spnCountry.setFocusable(false);

        spnCity.setBackgroundResource(R.drawable.spinner_back_city_u);
        spnCity.setClickable(false);
        spnCity.setFocusable(false);

        spnProvince.setBackgroundResource(R.drawable.spinner_back_state_u);
        spnProvince.setFocusable(false);
        spnProvince.setClickable(false);
        etZipCode.getEditText().setFocusable(false);
        etAddress.getEditText().setFocusable(false);
        etAboutMe.getEditText().setFocusable(false);
    }
    View.OnClickListener mUploadImageClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isEditProfile) {
                if (isCameraPermissionGranted()) {
                    showPictureDialog();
                } else {
                    requestPermissionForCamera();
                }
            }
        }
    };

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }
    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }
    void chooseImage(){
        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                 //   String path = saveImage(bitmap);
                    bitmap= Utils.scaleDown(bitmap, 800, true);
                    imgProfile.setImageBitmap(bitmap);
                    String profileImage= Utils.getBase64Image(bitmap);
                    if(C.isloggedIn) {
                        profile.setProfilePic(profileImage);
                        profile.setProfilePicname(Utils.getCurrentTimeStamp()+".jpg");
                    }
                    else {
                        profileDetail.setPicture(profileImage);
                        profileDetail.setPicturename(Utils.getCurrentTimeStamp()+".jpg");
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            bitmap= Utils.scaleDown(bitmap, 800, true);
            imgProfile.setImageBitmap(bitmap);
            String profileImage= Utils.getBase64Image(bitmap);
            if(C.isloggedIn) {
                profile.setProfilePic(profileImage);
                profile.setProfilePicname(Utils.getCurrentTimeStamp()+".jpg");
            }
            else {
                profileDetail.setPicture(profileImage);
                profileDetail.setPicturename(Utils.getCurrentTimeStamp()+".jpg");
            }
         //   saveImage(thumbnail);

        }

        /*if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                imgProfile.setImageBitmap(bitmap);
                String profileImage= Utils.getBase64Image(bitmap);

                if(C.isloggedIn) {
                   profile.setProfilePic(profileImage);
                    profile.setProfilePicname(Utils.getCurrentTimeStamp()+".jpg");
                }
                else {
                    profileDetail.setPicture(profileImage);
                    profileDetail.setPicturename(Utils.getCurrentTimeStamp()+".jpg");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    View.OnClickListener mNextClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            submit();
        }
    };


    void submit(){
        if(validation.validateAllEditText()) {
            if(isAllValid()) {
                if(util.isInternetOn(getActivity())) {

                    if (!SharedPreference.getInstance(getActivity()).getBoolean(C.IS_LOGIN)) {

                        profileDetail.setFirstname(edFirstName.getEditText().getText().toString());
                        profileDetail.setMiddlename(edMiddleName.getEditText().getText().toString());
                        profileDetail.setLastname(edLastName.getEditText().getText().toString());
                        profileDetail.setGender(spnGender.getSelectedItem().toString());
                        profileDetail.setSkype(edSkypeID.getEditText().getText().toString());
                        profileDetail.setPhone(Utils.getSubstringPhone(edPrimary.getEditText().getText().toString()));
                        if(edSecondary.getEditText().length()>8) {
                            profileDetail.setMobile1(Utils.getSubstringPhone(edSecondary.getEditText().getText().toString()));
                        }

                           /* if (spnGender.getSelectedItem().toString().equalsIgnoreCase("Male")) {
                                profileDetail.setGender("Male");

                            } else {
                                profileDetail.setGender("Female");

                            }*/
                        profileDetail.setGender(spnGender.getSelectedItem().toString());
                        profileDetail.setPassword(edPassword.getEditText().getText().toString());
                        profileDetail.setDob(Utils.getFormattedDate(spnDateOfBirth.getText().toString(), C.DATE_FORMAT, C.DESIRED_FORMAT));
                        profileDetail.setEmail(edEmail.getEditText().getText().toString());
                        profileDetail.setSocialid(profileDtl.getSocialid());
                        profileDetail.setMarital(spnMaritalStatus.getSelectedItem().toString());


                        profileDetail.setOrganization(etOraginization.getEditText().getText().toString());
                        profileDetail.setDesignation(etDesignation.getEditText().getText().toString());
                        if(tvWorkingSince.getText().toString()!=null && !tvWorkingSince.getText().toString().equals("")){
                            profileDetail.setWorking(Utils.getFormattedDate(tvWorkingSince.getText().toString(), C.DATE_FORMAT, C.DESIRED_FORMAT));
                        }
                        else {
                            profileDetail.setWorking("");

                        }
                        profileDetail.setAddress(etAddress.getEditText().getText().toString());
                        profileDetail.setAbout(etAboutMe.getEditText().getText().toString());
                        profileDetail.setZip(etZipCode.getEditText().getText().toString());
                           /* Bundle bundle = new Bundle();
                            bundle.putSerializable(C.DATA, profileDetail);
                            Intent i = new Intent(getContext(), FragmentProfessionalDetail.class);
                            i.putExtra("details", bundle);
                            startActivity(i);*/

                        Gson gson = new Gson();
                        String obj = gson.toJson(profileDetail);


                        try {
                            Log.e("DEBUG", "profileDetail=" + obj);
                            JSONObject jsonObject = new JSONObject(obj);
                            action = C.REGISTER_COUNTINUE_METHOD;
                            mIFragmentProfessionalDetailPresenter.registerDetail(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {

                        profile.setFirstName(edFirstName.getEditText().getText().toString());
                        profile.setMiddleName(edMiddleName.getEditText().getText().toString());
                        profile.setLastName(edLastName.getEditText().getText().toString());
                        profile.setMaritalStatus(spnMaritalStatus.getSelectedItem().toString());
                        profile.setSkype(edSkypeID.getEditText().getText().toString());
                        profile.setPhone(Utils.getSubstringPhone(edPrimary.getEditText().getText().toString()));
                        profile.setEmailId(edEmail.getEditText().getText().toString());
                        if(edSecondary.getEditText().getText().toString().length()>8) {
                            profile.setMobile(Utils.getSubstringPhone(edSecondary.getEditText().getText().toString()));
                        }
                           /* if (spnGender.getSelectedItem().toString().equalsIgnoreCase("Male")) {
                                profile.setGender("Male");
                            } else {
                                profile.setGender("Female");
                            }*/
                        profile.setGender(spnGender.getSelectedItem().toString());

                        profile.setDateOfBirth(Utils.getFormattedDate(spnDateOfBirth.getText().toString(), C.DATE_FORMAT, C.DESIRED_FORMAT));
                        profile.setToken(SharedPreference.getInstance(getActivity()).getString(C.TOKEN));

                        profile.setOrganization(etOraginization.getEditText().getText().toString());
                        profile.setDesignation(etDesignation.getEditText().getText().toString());

                        if(tvWorkingSince.getText().toString()!=null && !tvWorkingSince.getText().toString().equals("")){
                            profile.setWorkingSince(Utils.getFormattedDate(tvWorkingSince.getText().toString(), C.DATE_FORMAT, C.DESIRED_FORMAT));
                        }
                        else {
                            profile.setWorkingSince("");

                        }
                        profile.setZipCode(etZipCode.getEditText().getText().toString());
                        profile.setAddress(etAddress.getEditText().getText().toString());
                        profile.setAbout(etAboutMe.getEditText().getText().toString());
                        Gson gson = new Gson();
                        String obj = gson.toJson(profile);
                        Log.e("DEBUG", "profile=" + obj);
                        try {
                            action = C.UPDATE_PROFILE_METHOD;
                            JSONObject jsonObject = new JSONObject(obj);
                            mIFragmentProfessionalDetailPresenter.updateDetail(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                         /*   Bundle bundle = new Bundle();
                            bundle.putSerializable(C.PROFILE_METHOD, profile);
                            bundle.putSerializable(C.IS_EDIT_PROFILE, isEditProfile);

                            Intent i = new Intent(getContext(), FragmentProfessionalDetail.class);
                            i.putExtra("details", bundle);
                            startActivity(i);*/
                    }
                }
                else {
                    getDailogConfirm(getString(R.string.internet_issue)
                            , "Internet Issue");
                }
            }
        }
    }
    boolean isAllValid(){
        try {

            if (isEditProfile) {
                if (!C.isloggedIn && !edPassword.getEditText().getText().toString().equals(edConfirmPassword.getEditText().getText().toString())) {
                    edConfirmPassword.getEditText().setError(getString(R.string.password_validate));
                    return false;
                } else if (spnGender.getSelectedItem().toString().equals(C.SELECT_GENDER)) {
                    Utils.showToast(getActivity(), getString(R.string.select_gender));
                    return false;
                } else if (tvDob.getText().toString().equals("")) {
                    Utils.showToast(getActivity(), getString(R.string.enter_dob));

                    return false;
                } else if (spnMaritalStatus.getSelectedItem().toString().equals(C.SELECT_MARITAL_STATUS)) {
                    Utils.showToast(getActivity(), getString(R.string.selectMarital_status));
                    return false;
                } else if (spnCategory.getSelectedItem().toString().equals(C.SELECT_CATEGORY)) {
                    Utils.showToast(getActivity(), getString(R.string.select_category));
                    return false;
                } else if (spnCountry.getSelectedItem().toString().equals(C.SELECT_COUNTRY)) {
                    Utils.showToast(getActivity(), getString(R.string.select_country));
                    return false;
                } else if (spnCity.getSelectedItem().toString().equals(C.SELECT_CITY)) {
                    Utils.showToast(getActivity(), getString(R.string.select_city));
                    return false;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIFragmentBasicDetailPresenter.onDestroy();
    }

    void getProfileDetail(){
        if(SharedPreference.getInstance(getActivity()).getBoolean(C.IS_LOGIN)) {
            JSONObject jsonBody = new JSONObject();
            try {
             /*   jsonBody.put(C.EMAIL, "pradeep.bansal@techmobia.com");
                jsonBody.put(C.PASSWORD, "abc123");
                jsonBody.put(C.SOCAIL_ID, "");*/
                action=C.PROFILE_METHOD;
                jsonBody.put(C.MEMBER_ID, SharedPreference.getInstance(getActivity()).getString(C.MEMBER_ID));
                jsonBody.put(C.TOKEN, SharedPreference.getInstance(getActivity()).getString(C.TOKEN));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            mIFragmentBasicDetailPresenter.getBasicDetail(jsonBody);
        }
    }



    void showDetails(Profile profile){
        try {
            edFirstName.getEditText().setText(profile.getFirstName());
            edMiddleName.getEditText().setText(profile.getMiddleName());
            edLastName.getEditText().setText(profile.getLastName());
            if(profile.getPhone()!=null && profile.getPhone().length()>1) {
                edPrimary.getEditText().setFocusable(false);
            }
            edEmail.getEditText().setFocusable(false);

            edPrimary.getEditText().setText(profile.getPhone());
            edSecondary.getEditText().setText(profile.getMobile());
            edEmail.getEditText().setText(profile.getEmailId());

            edSkypeID.getEditText().setText(profile.getSkype());
            if (profile.getGender().equalsIgnoreCase("m") || profile.getGender().equalsIgnoreCase("male")) {
                spnGender.setSelection(1);
            } else {
                spnGender.setSelection(2);
            }
            if (profile.getMaritalStatus().equalsIgnoreCase(C.SINGLE)) {
                spnMaritalStatus.setSelection(1);
            } else if(profile.getMaritalStatus().equalsIgnoreCase(C.MARRIED)) {
                spnMaritalStatus.setSelection(2);
            }
            else if(profile.getMaritalStatus().equalsIgnoreCase(C.WIDOWED)) {
                spnMaritalStatus.setSelection(3);
            }
            else if(profile.getMaritalStatus().equalsIgnoreCase(C.SEAPRATED)) {
                spnMaritalStatus.setSelection(4);
            }
            else if(profile.getMaritalStatus().equalsIgnoreCase(C.DIVORCED)) {
                spnMaritalStatus.setSelection(5);
            }

            spnDateOfBirth.setText(Utils.getFormattedDate(profile.getDateOfBirth(), C.SERVER_DATE_FORMAT, C.DATE_FORMAT));
            if (profile.getProfilePic() != null && !profile.getProfilePic().equals("")) {
                if(!isEditProfile) {
                    tvUpload.setVisibility(View.GONE);
                }
                Utils.displayImage(getActivity(), C.IMAGE_BASE_URL + profile.getProfilePic(), imgProfile);
            //    Utils.displayImage(getActivity(), profile.getProfilePic(), imgProfile);
                //TODO Image Display
                profile.setProfilePicname(profile.getProfilePic());
                new AsyncGettingBitmapFromUrl().execute();
                // imgProfile.setImageBitmap(Utils.getImageBitmapFromByte64(profile.get(0).getProfilePic()));
            }

            etOraginization.getEditText().setText(profile.getOrganization());
            etDesignation.getEditText().setText(profile.getDesignation());
            etZipCode.getEditText().setText(profile.getZipCode());
            etAddress.getEditText().setText(profile.getAddress());
            etAboutMe.getEditText().setText(profile.getAbout());
            // tvWorkingSince.setText(Utils.getFormattedDate( profile.getWorkingSince(),"yyyy/MM/dd","dd/MM/yyyy"));
            tvWorkingSince.setText(Utils.getFormattedDate(profile.getWorkingSince(), C.SERVER_DATE_FORMAT, C.DATE_FORMAT));
            tvAboutMe.setText("ABOUT "+profile.getFirstName()+" "+profile.getLastName());

            if(!isEditProfile) {
                btnNext.setVisibility(View.GONE);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
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
                        Intent intent=new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra(C.SCREEN,C.FRAGMENT_LOGIN);
                        startActivity(intent);
                     /*   Intent intent=new Intent(getActivity(), ActivityHome.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra(C.SCREEN,C.FRAGMENT_SEARCH_RESULT);
                        startActivity(intent);*/


                    }
                   else if(titleText.equals("2")){
                        Intent intent=new Intent(getActivity(), ActivityHome.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Bundle mBundle = new Bundle();
                        mBundle.putBoolean(C.IS_EDIT_PROFILE,false);
                        mBundle.putBoolean(C.ADD_TO_BACK,false);
                        intent.putExtra(C.SCREEN,C.FRAGMENT_BASIC_DETAIL);
                        intent.putExtras(mBundle);
                        startActivity(intent);
                    }
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void getResponseSuccess(String response) {
        try {
            Log.e(FragmentBasicDetail.class.getName(),"RESPONSE=="+response);
            if (action.equals(C.PROFILE_METHOD)) {
                Gson gson = new Gson();
                ProfileStatus profileStatus = gson.fromJson(response, ProfileStatus.class);
                if (!profileStatus.getError()) {
                    profile = profileStatus.getMember().get(0);
                    getCategoryList();
                    showDetails(profile);
                } else {
                    if(profileStatus.getMessage().equals(C.InvalidToken)){
                        Utils.showToast(getActivity(), C.Session_expired);
                        SharedPreference.getInstance(getActivity()).setBoolean(C.IS_LOGIN,false);
                        Intent intent=new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra(C.SCREEN,C.FRAGMENT_LOGIN);
                        startActivity(intent);
                    }
                    else{
                        getDailogConfirm(profileStatus.getMessage(), "");

                    }
                }
            }
          else  if (action.equals(C.LOGIN_METHOD)) {
                Log.e(FragmentLogin.class.getName(), "RESPONSE==" + response);
                // Toast.makeText(getActivity(),response,Toast.LENGTH_LONG).show();

                Gson gson = new Gson();
                LoginStatus loginStatus = gson.fromJson(response, LoginStatus.class);
                if (!loginStatus.getError()) {
                    SharedPreference.getInstance(getActivity()).setString(C.TOKEN, loginStatus.getMember().getToken());
                    SharedPreference.getInstance(getActivity()).setString(C.MEMBER_ID, loginStatus.getMember().getMemberId());
                    SharedPreference.getInstance(getActivity()).setString(C.EMAIL, loginStatus.getMember().getEmailId());
                    SharedPreference.getInstance(getActivity()).setBoolean(C.IS_LOGIN, true);
                    Intent intent = new Intent(getActivity(), ActivityHome.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    getDailogConfirm(loginStatus.getMessage(), "");
                }
            } else if (action.equals(C.CATEGORY_LIST_METHOD)) {
                Gson gson = new Gson();
                categoryList = gson.fromJson(response, CategoryList.class);
                if (!categoryList.getError()) {
                    String[] catArr = new String[categoryList.getCategory().size() + 1];
                    catArr[0] = C.SELECT_CATEGORY;
                    for (int i = 0; i < categoryList.getCategory().size(); i++) {

                        catArr[i + 1] = String.valueOf(categoryList.getCategory().get(i).getCategoryName());

                    }

                /*ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, catArr); //selected item will look like a spinner set from XML

                spnCategory.setAdapter(spinnerArrayAdapter);*/
                    setCategoryList(Arrays.asList(catArr));
                    spinnerCategoryAdapter.notifyDataSetChanged();
                    if (C.isloggedIn) {
                        int pos = spinnerCategoryAdapter.getPosition(profile.getCategoryName());
                        spnCategory.setSelection(pos);
                    }
                    getCountryList();

                }
            } else if (action.equals(C.COUNTRY_METHOD)) {
                Gson gson = new Gson();
                countryList = gson.fromJson(response, CountryList.class);
                if (!countryList.getError()) {
                    String[] countArr = new String[countryList.getCountry().size() + 1];
                    countArr[0] = C.SELECT_COUNTRY;
                    for (int i = 0; i < countryList.getCountry().size(); i++) {
                        countArr[i + 1] = String.valueOf(countryList.getCountry().get(i).getName());
                    }

               /* ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, countArr); //selected item will look like a spinner set from XML

                spnCountry.setAdapter(spinnerArrayAdapter);*/
                    setCountryList(Arrays.asList(countArr));

                    spinnerCountryAdapter.notifyDataSetChanged();
                    if (C.isloggedIn) {
                        int pos = spinnerCountryAdapter.getPosition(profile.getCountryName());
                        spnCountry.setSelection(pos, false);
                    }

                } else {
                    String[] countArr = new String[1];
                    countArr[0] = C.SELECT_COUNTRY;
               /* ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, countArr); //selected item will look like a spinner set from XML
                spnCountry.setAdapter(spinnerArrayAdapter);*/
                    setCountryList(Arrays.asList(countArr));

                    spinnerCountryAdapter.notifyDataSetChanged();

                }
            } else if (action.equals(C.STATE_METHOD)) {
                Gson gson = new Gson();
                stateList = gson.fromJson(response, StateList.class);
                if (!stateList.getError()) {
                    String[] stateArr = new String[stateList.getState().size() + 1];
                    stateArr[0] = C.SELECT_STATE;
                    for (int i = 0; i < stateList.getState().size(); i++) {
                        stateArr[i + 1] = String.valueOf(stateList.getState().get(i).getName());
                    }


               /* ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stateArr); //selected item will look like a spinner set from XML
                spnProvince.setAdapter(spinnerArrayAdapter);*/
                    setStateList(Arrays.asList(stateArr));
                    spinnerStateAdapter.notifyDataSetChanged();
                    if (C.isloggedIn) {
                        spnProvince.setSelection(spinnerStateAdapter.getPosition(profile.getStateName()));
                    }
                } else {
                    String[] stateArr = new String[1];
                    stateArr[0] = C.SELECT_STATE;
               /* ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stateArr); //selected item will look like a spinner set from XML
                spnProvince.setAdapter(spinnerArrayAdapter);*/
                    setStateList(Arrays.asList(stateArr));
                    spinnerStateAdapter.notifyDataSetChanged();

                }
            } else if (action.equals(C.CITY_METHOD)) {
                Gson gson = new Gson();
                cityList = gson.fromJson(response, CityList.class);
                if (!cityList.getError()) {
                    String[] cityArr = new String[cityList.getCity().size() + 1];
                    cityArr[0] = C.SELECT_CITY;
                    for (int i = 0; i < cityList.getCity().size(); i++) {
                        cityArr[i + 1] = String.valueOf(cityList.getCity().get(i).getName());
                    }

             /*   ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cityArr); //selected item will look like a spinner set from XML
                spnCity.setAdapter(spinnerArrayAdapter);*/
                    setCityList(Arrays.asList(cityArr));

                    spinnerCityAdapter.notifyDataSetChanged();
                    if (C.isloggedIn) {
                        spnCity.setSelection(spinnerCityAdapter.getPosition(profile.getCityName()));
                    }
                } else {
                    String[] cityArr = new String[1];
                    cityArr[0] = C.SELECT_CITY;
              /*  ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cityArr); //selected item will look like a spinner set from XML
                spnCity.setAdapter(spinnerArrayAdapter);*/
                    setCityList(Arrays.asList(cityArr));

                    spinnerCityAdapter.notifyDataSetChanged();
                    // spinnerArrayAdapter.notifyDataSetChanged();
                }
            } else if (action.equals(C.REGISTER_COUNTINUE_METHOD)) {
                Gson gson = new Gson();
                RegisterStatus registerStatus = gson.fromJson(response, RegisterStatus.class);
                if (!registerStatus.getError()) {
                    getDailogConfirm(registerStatus.getMessage(), "1");
                } else {

                    if (registerStatus.getMessage().equals(C.InvalidToken)) {
                        Utils.showToast(getActivity(), C.Session_expired);
                        SharedPreference.getInstance(getActivity()).setBoolean(C.IS_LOGIN,false);

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra(C.SCREEN, C.FRAGMENT_LOGIN);
                        startActivity(intent);
                    } else {
                        getDailogConfirm(registerStatus.getMessage(), "");
                    }
                }

            } else if (action.equals(C.UPDATE_PROFILE_METHOD)) {
                Gson gson = new Gson();
                ProfileStatus profileStatus = gson.fromJson(response, ProfileStatus.class);
                if (!profileStatus.getError()) {
                    getDailogConfirm(profileStatus.getMessage(), "2");

                } else {

                    if (profileStatus.getMessage().equals(C.InvalidToken)) {
                        Utils.showToast(getActivity(), C.Session_expired);
                        SharedPreference.getInstance(getActivity()).setBoolean(C.IS_LOGIN,false);

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra(C.SCREEN, C.FRAGMENT_LOGIN);
                        startActivity(intent);
                    }
                    else {
                        getDailogConfirm(profileStatus.getMessage(), "");
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void getResponseError(String response) {
        Log.e(FragmentBasicDetail.class.getName(),"RESPONSE=="+response);
        if (action.equals(C.UPDATE_PROFILE_METHOD) ||  action.equals(C.PROFILE_METHOD)  ||action.equals(C.UPDATE_PROFILE_METHOD)) {
            getDailogConfirm(getResources().getString(R.string.slow_network), "");
        }
    }

    @Override
    public void showProgress() {
        util.showDialog(C.MSG,getActivity());
    }

    @Override
    public void hideProgress() {
        util.hideDialog();
    }
    void getCountryList() {
        action = C.COUNTRY_METHOD;
        if(C.isloggedIn) {
            mIFragmentProfessionalDetailPresenter.getCountryList(null, false);
        }
        else {
            mIFragmentProfessionalDetailPresenter.getCountryList(null, true);

        }
    }
    void setCategoryList( List<String> catList){
        try {

            spinnerCategoryAdapter = new ArrayAdapter<String>(
                    getActivity(), R.layout.spinner_item, catList) {
                @Override
                public boolean isEnabled(int position) {
                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            spinnerCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnCategory.setAdapter(spinnerCategoryAdapter);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    void setCountryList( List<String> countryList){
        try {
            spinnerCountryAdapter = new ArrayAdapter<String>(
                    getActivity(), R.layout.spinner_item, countryList) {
                @Override
                public boolean isEnabled(int position) {
                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            spinnerCountryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnCountry.setAdapter(spinnerCountryAdapter);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    void setStateList( List<String> StateList){
        try {
            spinnerStateAdapter = new ArrayAdapter<String>(
                    getActivity(), R.layout.spinner_item, StateList) {
                @Override
                public boolean isEnabled(int position) {
                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            spinnerStateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnProvince.setAdapter(spinnerStateAdapter);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    void setCityList( List<String> cityList){
        try {

            spinnerCityAdapter = new ArrayAdapter<String>(
                    getActivity(), R.layout.spinner_item, cityList) {
                @Override
                public boolean isEnabled(int position) {
                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            spinnerCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnCity.setAdapter(spinnerCityAdapter);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private class AsyncGettingBitmapFromUrl extends AsyncTask<Void, Void, Bitmap> {




        @Override
        protected Bitmap doInBackground(Void... params) {
            return Utils.getBitmapFromUrl(C.IMAGE_BASE_URL+profile.getProfilePic());

            /*try {
                URL url = new URL(C.IMAGE_BASE_URL+profile.getProfilePic());
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return image;
            } catch(IOException e) {
                System.out.println(e);
            }
            return null;*/
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

           if(bitmap!=null)
            profile.setProfilePic(Utils.getBase64Image(bitmap));

        }
    }
    public  boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {


                // ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation

            return true;
        }


    }

    private void requestPermissionForCamera(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.CAMERA)){
            //     ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Utils.PERMISSION_REQUEST_CODE);
            getDailogConfirm("Please allow camera permission in App Settings for additional functionality.", "4");
            //  Toast.makeText(getActivity(),"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, 2);
        }
    }


    void getDialPad(String dial_number){
        Intent dial = new Intent();
        dial.setAction("android.intent.action.DIAL");
        dial.setData(Uri.parse("tel:"+dial_number));
        startActivity(dial);
    }
}
