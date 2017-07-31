package com.softmine.dooktravel.view.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.softmine.dooktravel.view.ActivityHome;
import com.softmine.dooktravel.view.MainActivity;
import com.softmine.dooktravel.R;
import com.softmine.dooktravel.pojos.CategoryList;
import com.softmine.dooktravel.pojos.CityList;
import com.softmine.dooktravel.pojos.CountryList;
import com.softmine.dooktravel.model.LoginStatus;
import com.softmine.dooktravel.pojos.Profile;
import com.softmine.dooktravel.pojos.ProfileDetail;
import com.softmine.dooktravel.pojos.ProfileStatus;
import com.softmine.dooktravel.pojos.RegisterStatus;
import com.softmine.dooktravel.pojos.StateList;
import com.softmine.dooktravel.serviceconnection.CompleteListener;
import com.softmine.dooktravel.serviceconnection.ServiceConnection;
import com.softmine.dooktravel.util.C;
import com.softmine.dooktravel.util.SharedPreference;
import com.softmine.dooktravel.util.Utils;
import com.softmine.dooktravel.validations.ValidateEditText;
import com.softmine.dooktravel.validations.Validations;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfessionalDetail extends AppCompatActivity implements CompleteListener {
    String action;
    ProfileDetail profileDetail;
    Button btnSubmit;
    TextView tvProfesstionalDetail, tvCategory, tvWorkingHour, tvWorkingSince,
            tvCurrentAdd, tvCountry, tvCity, tvProvince, tvAboutMe, tvPostal,tvTextCount;
    ValidateEditText etOraginization, etDesignation, etAddress, etAboutMe, etZipCode;
    Spinner spnCategory, spnCountry, spnCity, spnProvince;
    CountryList countryList;
    StateList stateList;
    CityList cityList;
    CategoryList categoryList;
    int flags;
    Validations validation ;
    Utils utils;
    private SimpleDateFormat dateFormatter;
    private int year;
    private int month;
    private int day;
    private DatePickerDialog DatePickerDialog;
    Profile profile;
    int mCountryPos = 0, mCityPos = 0, mStatePos = 0, mCategoryPos = 0;

    public FragmentProfessionalDetail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getBundleExtra("details");
        setContentView(R.layout.fragment_fragment_professional_detail);
        if (bundle != null) {
            if (C.isloggedIn) {
                profile = (Profile) bundle.getSerializable(C.PROFILE_METHOD);
            } else {
                profileDetail = (ProfileDetail) bundle.getSerializable(C.DATA);
            }
        }
        onViewCreated(this, bundle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow));
        setTitle(R.string.professional_detail);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void onViewCreated(Activity view, @Nullable Bundle savedInstanceState) {
        validation = new Validations();
        utils=new Utils();
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(mBtnSubmitCLickLisner);
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
                DatePickerDialog.show();
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
        tvTextCount=(TextView)view.findViewById(R.id.tvTextCount);
        spnCategory = (Spinner) view.findViewById(R.id.spinner_category);

        spnCategory.setOnItemSelectedListener(mOnCategorySelectedListner);
        spnCountry = (Spinner) view.findViewById(R.id.spinner_country);
        spnCountry.setOnItemSelectedListener(mOnCountrySelectedListner);
        spnCity = (Spinner) view.findViewById(R.id.spinner_city);
        spnProvince = (Spinner) view.findViewById(R.id.spinner_province);
        spnProvince.setOnItemSelectedListener(mOnStateItemSelectedListner);
        spnCity.setOnItemSelectedListener(mOnCityItemSelectedListner);

        btnSubmit.setTypeface(Utils.getSemiBoldTypeFace(getActivity()));

        btnSubmit.setTypeface(Utils.getSemiBoldTypeFace(getActivity()));
        tvProfesstionalDetail.setTypeface(Utils.getRegularTypeFace(getActivity()));
        tvCategory.setTypeface(Utils.getLightTypeFace(getActivity()));
        tvWorkingHour.setTypeface(Utils.getLightTypeFace(getActivity()));
        tvCurrentAdd.setTypeface(Utils.getRegularTypeFace(getActivity()));
        tvCountry.setTypeface(Utils.getLightTypeFace(getActivity()));
        tvCity.setTypeface(Utils.getLightTypeFace(getActivity()));
        tvTextCount.setTypeface(Utils.getRegularTypeFace(getActivity()));
        tvProvince.setTypeface(Utils.getLightTypeFace(getActivity()));
        etZipCode.getEditText().setTypeface(Utils.getLightTypeFace(getActivity()));
        tvAboutMe.setTypeface(Utils.getRegularTypeFace(getActivity()));
        tvWorkingSince.setTypeface(Utils.getRegularTypeFace(getActivity()));
        tvPostal.setTypeface(Utils.getRegularTypeFace(getActivity()));

        etOraginization.getEditText().setTypeface(Utils.getRegularTypeFace(getActivity()));
        etDesignation.getEditText().setTypeface(Utils.getRegularTypeFace(getActivity()));
        etAddress.getEditText().setTypeface(Utils.getRegularTypeFace(getActivity()));
        etAboutMe.getEditText().setTypeface(Utils.getRegularTypeFace(getActivity()));
        etAboutMe.getEditText().addTextChangedListener(mTextChangeListner);
        Calendar newCalendar = Calendar.getInstance();
        year = newCalendar.get(Calendar.YEAR);
        month = newCalendar.get(Calendar.MONTH);
        day = newCalendar.get(Calendar.DAY_OF_MONTH);
        dateFormatter = new SimpleDateFormat(C.DATE_FORMAT);
        DatePickerDialog = new DatePickerDialog(getActivity(), new android.app.DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int y, int monthOfYear, int dayOfMonth) {
                year = y;
                month = monthOfYear;
                day = dayOfMonth;
                Calendar newDate = Calendar.getInstance();
                newDate.set(y, monthOfYear, dayOfMonth);
                tvWorkingSince.setText(dateFormatter.format(newDate.getTime()));
            }

        }, year, month, day);

        if (C.isloggedIn) {
            etOraginization.getEditText().setText(profile.getOrganization());
            // etDesignation.getEditText().setText(profile.);
            etZipCode.getEditText().setText(profile.getZipCode());
            etAddress.getEditText().setText(profile.getAddress());
            etAboutMe.getEditText().setText(profile.getAbout());
           // tvWorkingSince.setText(Utils.getFormattedDate( profile.getWorkingSince(),"yyyy/MM/dd","dd/MM/yyyy"));
            tvWorkingSince.setText(Utils.getFormattedDate(profile.getWorkingSince(),C.SERVER_DATE_FORMAT,C.DATE_FORMAT));


        }
        getCategoryList();

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

    void getCategoryList() {
        action = C.CATEGORY_LIST_METHOD;
        ServiceConnection serviceConnection = new ServiceConnection();
        serviceConnection.makeJsonObjectRequest(C.CATEGORY_LIST_METHOD, null, FragmentProfessionalDetail.this);
    }

    View.OnClickListener mBtnSubmitCLickLisner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // ((ActivityHome)getActivity()).fragmnetLoader(C.FRAGMENT_SEARCH_RESULT,null);
            // Map<String,String> params = new HashMap<String, String>();
            if(utils.isInternetOn(FragmentProfessionalDetail.this)) {
                if (C.isloggedIn) {
                    //    Toast.makeText(getActivity(),"Under development",Toast.LENGTH_LONG).show();
                    profile.setOrganization(etOraginization.getEditText().getText().toString());
                    profile.setWorkingSince(Utils.getFormattedDate(tvWorkingSince.getText().toString(), C.DATE_FORMAT, C.DESIRED_FORMAT));
                    profile.setZipCode(etZipCode.getEditText().getText().toString());
                    profile.setAddress(etAddress.getEditText().getText().toString());
                    profile.setAbout(etAboutMe.getEditText().getText().toString());

                    Gson gson = new Gson();
                    String obj = gson.toJson(profile);
                    Log.e("DEBUG", "profile=" + obj);
                    try {
                        action = C.UPDATE_PROFILE_METHOD;
                        JSONObject jsonObject = new JSONObject(obj);
                        ServiceConnection serviceConnection = new ServiceConnection();
                        serviceConnection.makeJsonObjectRequest(C.UPDATE_PROFILE_METHOD, jsonObject, FragmentProfessionalDetail.this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    profileDetail.setOrganization(etOraginization.getEditText().getText().toString());
                    profileDetail.setWorking(Utils.getFormattedDate(tvWorkingSince.getText().toString(), C.DATE_FORMAT, C.DESIRED_FORMAT));
                    profileDetail.setAddress(etAddress.getEditText().getText().toString());
                    profileDetail.setAbout(etAboutMe.getEditText().getText().toString());
                    profileDetail.setZip(etZipCode.getEditText().getText().toString());
                    Gson gson = new Gson();
                    String obj = gson.toJson(profileDetail);


                    try {
                        Log.e("DEBUG", "profile=" + obj);
                        JSONObject jsonObject = new JSONObject(obj);
                        action = C.REGISTER_COUNTINUE_METHOD;
                        ServiceConnection serviceConnection = new ServiceConnection();
                        serviceConnection.makeJsonObjectRequest(C.REGISTER_COUNTINUE_METHOD, jsonObject, FragmentProfessionalDetail.this);
                        //     Toast.makeText(getActivity(),"Under development",Toast.LENGTH_LONG).show();
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
    };

    @Override
    public void done(String response) {
        Log.e(FragmentProfessionalDetail.class.getName(), "RESPONSE==" + response);
        if (action.equals(C.LOGIN_METHOD)) {
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
                catArr[0] = C.SELECT;
                for (int i = 0; i < categoryList.getCategory().size(); i++) {

                    catArr[i + 1] = String.valueOf(categoryList.getCategory().get(i).getCategoryName());

                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, catArr); //selected item will look like a spinner set from XML

                spnCategory.setAdapter(spinnerArrayAdapter);
                spinnerArrayAdapter.notifyDataSetChanged();
                if (C.isloggedIn) {
                    int pos = spinnerArrayAdapter.getPosition(profile.getCategoryName());
                    spnCategory.setSelection(pos);
                }
                getCountryList();

            }
        } else if (action.equals(C.COUNTRY_METHOD)) {
            Gson gson = new Gson();
            countryList = gson.fromJson(response, CountryList.class);
            if (!countryList.getError()) {
                String[] countArr = new String[countryList.getCountry().size() + 1];
                countArr[0] = C.SELECT;
                for (int i = 0; i < countryList.getCountry().size(); i++) {
                    countArr[i + 1] = String.valueOf(countryList.getCountry().get(i).getName());
                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, countArr); //selected item will look like a spinner set from XML

                spnCountry.setAdapter(spinnerArrayAdapter);
                spinnerArrayAdapter.notifyDataSetChanged();
                if (C.isloggedIn) {
                    int pos = spinnerArrayAdapter.getPosition(profile.getCountryName());
                    spnCountry.setSelection(pos, false);
                }

            } else {
                String[] countArr = new String[1];
                countArr[0] = C.SELECT;
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, countArr); //selected item will look like a spinner set from XML
                spnCountry.setAdapter(spinnerArrayAdapter);
                spinnerArrayAdapter.notifyDataSetChanged();

            }
        } else if (action.equals(C.STATE_METHOD)) {
            Gson gson = new Gson();
            stateList = gson.fromJson(response, StateList.class);
            if (!stateList.getError()) {
                String[] stateArr = new String[stateList.getState().size() + 1];
                stateArr[0] = C.SELECT;
                for (int i = 0; i < stateList.getState().size(); i++) {
                    stateArr[i + 1] = String.valueOf(stateList.getState().get(i).getName());
                }


                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stateArr); //selected item will look like a spinner set from XML
                spnProvince.setAdapter(spinnerArrayAdapter);
                spinnerArrayAdapter.notifyDataSetChanged();
                if (C.isloggedIn) {
                    spnProvince.setSelection(spinnerArrayAdapter.getPosition(profile.getStateName()));
                }
            } else {
                String[] stateArr = new String[1];
                stateArr[0] = C.SELECT;
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stateArr); //selected item will look like a spinner set from XML
                spnProvince.setAdapter(spinnerArrayAdapter);
                spinnerArrayAdapter.notifyDataSetChanged();

            }
        } else if (action.equals(C.CITY_METHOD)) {
            Gson gson = new Gson();
            cityList = gson.fromJson(response, CityList.class);
            if (!cityList.getError()) {
                String[] cityArr = new String[cityList.getCity().size() + 1];
                cityArr[0] = C.SELECT;
                for (int i = 0; i < cityList.getCity().size(); i++) {
                    cityArr[i + 1] = String.valueOf(cityList.getCity().get(i).getName());
                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cityArr); //selected item will look like a spinner set from XML
                spnCity.setAdapter(spinnerArrayAdapter);
                spinnerArrayAdapter.notifyDataSetChanged();
                if (C.isloggedIn) {
                    spnCity.setSelection(spinnerArrayAdapter.getPosition(profile.getCityName()));
                }
            } else {
                String[] cityArr = new String[1];
                cityArr[0] = C.SELECT;
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cityArr); //selected item will look like a spinner set from XML
                spnCity.setAdapter(spinnerArrayAdapter);
                spinnerArrayAdapter.notifyDataSetChanged();
            }
        } else if (action.equals(C.REGISTER_COUNTINUE_METHOD)) {
            Gson gson = new Gson();
            RegisterStatus registerStatus = gson.fromJson(response, RegisterStatus.class);
            if(!registerStatus.getError()) {
                getDailogConfirm(registerStatus.getMessage(), "1");
            }
            else {
                getDailogConfirm(registerStatus.getMessage(),"");
            }

        } else if (action.equals(C.UPDATE_PROFILE_METHOD)) {
            Gson gson = new Gson();
            ProfileStatus profileStatus = gson.fromJson(response, ProfileStatus.class);
            if (!profileStatus.getError()) {
                getDailogConfirm(profileStatus.getMessage(), "2");

            } else {
                getDailogConfirm(profileStatus.getMessage(), "");
            }
        }
    }

    void getCountryList() {
        action = C.COUNTRY_METHOD;
        ServiceConnection serviceConnection = new ServiceConnection();
        serviceConnection.serviceRequest(C.COUNTRY_METHOD, null, FragmentProfessionalDetail.this);
    }

    void getStateList(String countryId) {
        Log.e("DEBUG", "countryId=" + countryId);

        action = C.STATE_METHOD;

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put(C.COUNTRY_ID, countryId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ServiceConnection serviceConnection = new ServiceConnection();
        if(C.isloggedIn) {
            serviceConnection.serviceRequest(C.STATE_METHOD, jsonBody, FragmentProfessionalDetail.this);
        }
        else{
            serviceConnection.makeJsonObjectRequest(C.STATE_METHOD, jsonBody, FragmentProfessionalDetail.this);
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
        ServiceConnection serviceConnection = new ServiceConnection();
        if(C.isloggedIn) {
            serviceConnection.serviceRequest(C.CITY_METHOD, jsonBody, FragmentProfessionalDetail.this);
        }
        else{
            serviceConnection.makeJsonObjectRequest(C.CITY_METHOD, jsonBody, FragmentProfessionalDetail.this);
        }

    }

    @Override
    public Context getApplicationsContext() {
        return getActivity();
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
                      /*  if (validation.validateAllEditText()) {


                            JSONObject jsonBody = new JSONObject();
                            try {
                                jsonBody.put(C.EMAIL, profileDetail.getEmail());
                                jsonBody.put(C.PASSWORD, profileDetail.getPassword());
                                jsonBody.put(C.SOCAIL_ID, "");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            action = C.LOGIN_METHOD;
                            ServiceConnection serviceConnection = new ServiceConnection();
                            serviceConnection.makeJsonObjectRequest(C.LOGIN_METHOD, jsonBody, FragmentProfessionalDetail.this);
                        }*/
                        Intent intent=new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra(C.SCREEN,C.FRAGMENT_LOGIN);
                        startActivity(intent);
                    }
                    else if(titleText.equals("2")){
                        Intent intent=new Intent(getActivity(), ActivityHome.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Context getActivity() {
        return this;
    }
}
