package com.softmine.dooktravel.fragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.softmine.dooktravel.R;
import com.softmine.dooktravel.pojos.CategoryList;
import com.softmine.dooktravel.pojos.CityList;
import com.softmine.dooktravel.pojos.CountryList;
import com.softmine.dooktravel.pojos.Profile;
import com.softmine.dooktravel.pojos.ProfileDetail;
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
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfessionalDetail extends Fragment implements CompleteListener{
    String action;
    ProfileDetail profileDetail;
    Button btnSubmit;
    TextView tvProfesstionalDetail,tvCategory,tvWorkingHour,tvWorkingSince,tvCurrentAdd,tvCountry,tvCity,tvProvince,tvAboutMe,tvPostal;
    ValidateEditText etOraginization,etDesignation,etAddress,etAboutMe,etZipCode;
    Spinner spnCategory,spnCountry,spnCity,spnProvince;
    CountryList countryList;
    StateList stateList;
    CityList cityList;
    int flags;
    Validations validation = new Validations();

    private SimpleDateFormat dateFormatter;
    private int year;
    private int month;
    private int day;
    private DatePickerDialog DatePickerDialog;
    boolean isLogin=false;
    Profile profile;
    public FragmentProfessionalDetail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if(SharedPreference.getInstance(getActivity()).getBoolean(C.IS_LOGIN)) {
                isLogin=true;
                profile = (Profile) bundle.getSerializable(C.PROFILE_METHOD);
            }
            else {
                isLogin=false;
                profileDetail = (ProfileDetail) bundle.getSerializable(C.DATA);
            }
        }
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSubmit=(Button)view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(mBtnSubmitCLickLisner);
        tvProfesstionalDetail=(TextView)view.findViewById(R.id.tvProfessionalDetail);
        tvCategory=(TextView)view.findViewById(R.id.tvCategory);
        tvWorkingHour=(TextView)view.findViewById(R.id.tvWorkingHour);
        tvCurrentAdd=(TextView)view.findViewById(R.id.tvCurrentAddress);
        tvCountry=(TextView)view.findViewById(R.id.tvCountry);
        tvCity=(TextView)view.findViewById(R.id.tvCity);
        tvProvince=(TextView)view.findViewById(R.id.tvProvince);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        etZipCode=new ValidateEditText((EditText)view.findViewById(R.id.et_Postal),getActivity(),flags);
        tvAboutMe=(TextView)view.findViewById(R.id.tvAboutUs);
        tvWorkingSince=(TextView) view.findViewById(R.id.tv_working_since);
        tvWorkingSince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.show();
            }
        });
        tvPostal=(TextView) view.findViewById(R.id.et_Postal);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        etOraginization=new ValidateEditText((EditText)view.findViewById(R.id.edOrganization),getActivity(),flags);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        etDesignation=new ValidateEditText((EditText)view.findViewById(R.id.edDesignation),getActivity(),flags);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        etAddress=new ValidateEditText((EditText)view.findViewById(R.id.edAddress),getActivity(),flags);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        etAboutMe=new ValidateEditText((EditText)view.findViewById(R.id.edAboutMe),getActivity(),flags);

        spnCategory=(Spinner)view.findViewById(R.id.spinner_category);
        spnCountry=(Spinner)view.findViewById(R.id.spinner_country);
        spnCountry.setOnItemSelectedListener(mOnCountrySelectedListner);
        spnCity=(Spinner)view.findViewById(R.id.spinner_city);
        spnProvince=(Spinner)view.findViewById(R.id.spinner_province);
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
        tvProvince.setTypeface(Utils.getLightTypeFace(getActivity()));
        etZipCode.getEditText().setTypeface(Utils.getLightTypeFace(getActivity()));
        tvAboutMe.setTypeface(Utils.getRegularTypeFace(getActivity()));
        tvWorkingSince.setTypeface(Utils.getRegularTypeFace(getActivity()));
        tvPostal.setTypeface(Utils.getRegularTypeFace(getActivity()));

        etOraginization.getEditText().setTypeface(Utils.getRegularTypeFace(getActivity()));
        etDesignation.getEditText().setTypeface(Utils.getRegularTypeFace(getActivity()));
        etAddress.getEditText().setTypeface(Utils.getRegularTypeFace(getActivity()));
        etAboutMe.getEditText().setTypeface(Utils.getRegularTypeFace(getActivity()));

        Calendar newCalendar = Calendar.getInstance();
        year  = newCalendar.get(Calendar.YEAR);
        month = newCalendar.get(Calendar.MONTH);
        day   = newCalendar.get(Calendar.DAY_OF_MONTH);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        DatePickerDialog = new DatePickerDialog(getActivity(), new android.app.DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int y, int monthOfYear, int dayOfMonth) {
                year  = y;
                month = monthOfYear;
                day   = dayOfMonth;
                Calendar newDate = Calendar.getInstance();
                newDate.set(y, monthOfYear, dayOfMonth);
                tvWorkingSince.setText(dateFormatter.format(newDate.getTime()));
            }

        },year, month, day);

        if(isLogin){
            etOraginization.getEditText().setText(profile.getOrganization());
           // etDesignation.getEditText().setText(profile.);
            etZipCode.getEditText().setText(profile.getZipCode());
            etAddress.getEditText().setText(profile.getAddress());
            etAboutMe.getEditText().setText(profile.getAbout());
            tvWorkingSince.setText(profile.getWorkingSince());
        }
        getCategoryList();

    }


    AdapterView.OnItemSelectedListener mOnStateItemSelectedListner=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
           if(stateList!=null && position!=0) {
           //    profileDetail.setStateid(stateList.getState().get(position - 1).getId());
               getCityList(stateList.getState().get(position - 1).getId());
           }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener mOnCountrySelectedListner=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if(countryList!=null && position!=0) {
            //    profileDetail.setCountryid(countryList.getCountry().get(position - 1).getId());
                getStateList(countryList.getCountry().get(position - 1).getId());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener mOnCityItemSelectedListner=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(cityList!=null && position!=0){
                //profileDetail.setCityid(cityList.getCity().get(position-1).getId());
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    void  getCategoryList(){
        action=C.CATEGORY_LIST_METHOD;
        ServiceConnection serviceConnection=new ServiceConnection();
        serviceConnection.makeJsonObjectRequest(C.CATEGORY_LIST_METHOD,null,FragmentProfessionalDetail.this);
    }

    View.OnClickListener mBtnSubmitCLickLisner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           // ((ActivityHome)getActivity()).fragmnetLoader(C.FRAGMENT_SEARCH_RESULT,null);
           // Map<String,String> params = new HashMap<String, String>();
            if(isLogin) {
                Toast.makeText(getActivity(),"Under development",Toast.LENGTH_LONG).show();
            }
            else {
           /*     profileDetail.setOrganization(etOraginization.toString());
                profileDetail.setWorking(tvWorkingSince.getText().toString());
                profileDetail.setAddress(etAddress.toString());
                ServiceConnection serviceConnection = new ServiceConnection();
                serviceConnection.sendToServer(C.COUNTRY_METHOD, null, FragmentProfessionalDetail.this);*/
                Toast.makeText(getActivity(),"Under development",Toast.LENGTH_LONG).show();
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_professional_detail, container, false);
    }

    @Override
    public void done(String response) {
        Log.e(FragmentProfessionalDetail.class.getName(),"RESPONSE=="+response);

        if(action.equals(C.CATEGORY_LIST_METHOD)){
            Gson gson = new Gson();
            CategoryList categoryList= gson.fromJson(response,CategoryList.class);
            if(!categoryList.getError()){
            String[] catArr = new String[categoryList.getCategory().size()+1];
            catArr[0] = C.SELECT;
                for (int i=0;i<categoryList.getCategory().size();i++){

                catArr[i+1] = String.valueOf(categoryList.getCategory().get(i).getCategoryName());
            }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, catArr); //selected item will look like a spinner set from XML

                spnCategory.setAdapter(spinnerArrayAdapter);
                spinnerArrayAdapter.notifyDataSetChanged();

                profile.getCategoryName();
                getCountryList();

            }
        }
      else   if(action.equals(C.COUNTRY_METHOD)){
            Gson gson = new Gson();
            countryList= gson.fromJson(response,CountryList.class);
            if(!countryList.getError()){
            String[] countArr = new String[countryList.getCountry().size()+1];
            countArr[0] = C.SELECT;
            for (int i=0;i<countryList.getCountry().size();i++){
                countArr[i+1] = String.valueOf(countryList.getCountry().get(i).getName());
            }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, countArr); //selected item will look like a spinner set from XML

                spnCountry.setAdapter(spinnerArrayAdapter);
                spinnerArrayAdapter.notifyDataSetChanged();

            }
            else {
                String[] countArr = new String[1];
                countArr[0] = C.SELECT;
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, countArr); //selected item will look like a spinner set from XML
                spnCountry.setAdapter(spinnerArrayAdapter);
                spinnerArrayAdapter.notifyDataSetChanged();

            }
        }
        else   if(action.equals(C.STATE_METHOD)){
            Gson gson = new Gson();
            stateList= gson.fromJson(response,StateList.class);
            if(!stateList.getError()){
            String[] stateArr = new String[stateList.getState().size()+1];
            stateArr[0] = C.SELECT;
            for (int i=0;i<stateList.getState().size();i++){
                stateArr[i+1] = String.valueOf(stateList.getState().get(i).getName());
            }


                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stateArr); //selected item will look like a spinner set from XML
                spnProvince.setAdapter(spinnerArrayAdapter);
                spinnerArrayAdapter.notifyDataSetChanged();

            }
            else {
                String[] stateArr = new String[1];
                stateArr[0] = C.SELECT;
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stateArr); //selected item will look like a spinner set from XML
                spnProvince.setAdapter(spinnerArrayAdapter);
                spinnerArrayAdapter.notifyDataSetChanged();

            }
        }
        else   if(action.equals(C.CITY_METHOD)){
            Gson gson = new Gson();
            cityList= gson.fromJson(response,CityList.class);
            if(!cityList.getError()){
            String[] cityArr = new String[cityList.getCity().size()+1];
            cityArr[0] = C.SELECT;
            for (int i=0;i<cityList.getCity().size();i++){
                cityArr[i+1] = String.valueOf(cityList.getCity().get(i).getName());
            }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cityArr); //selected item will look like a spinner set from XML
                spnCity.setAdapter(spinnerArrayAdapter);
                spinnerArrayAdapter.notifyDataSetChanged();

            }

            else {
                String[] cityArr = new String[1];
                cityArr[0] = C.SELECT;
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cityArr); //selected item will look like a spinner set from XML
                spnCity.setAdapter(spinnerArrayAdapter);
                spinnerArrayAdapter.notifyDataSetChanged();
            }
        }
    }

   void getCountryList(){
       action=C.COUNTRY_METHOD;
       ServiceConnection serviceConnection=new ServiceConnection();
       serviceConnection.makeJsonObjectRequest(C.COUNTRY_METHOD,null,FragmentProfessionalDetail.this);
    }

    void getStateList(String countryId){
        Log.e("DEBUG","countryId="+countryId);

        action=C.STATE_METHOD;

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put(C.COUNTRY_ID, countryId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ServiceConnection serviceConnection=new ServiceConnection();
        serviceConnection.makeJsonObjectRequest(C.STATE_METHOD,jsonBody,FragmentProfessionalDetail.this);

    }

    void getCityList(String stateId){
        Log.e("DEBUG","StateID="+stateId);
        action=C.CITY_METHOD;

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put(C.STATE_ID, stateId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ServiceConnection serviceConnection=new ServiceConnection();
        serviceConnection.makeJsonObjectRequest(C.CITY_METHOD,jsonBody,FragmentProfessionalDetail.this);

    }
    @Override
    public Context getApplicationsContext() {
        return getActivity();
    }
}
