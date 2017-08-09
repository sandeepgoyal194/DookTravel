package com.softmine.dooktravel.view.fragments;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.softmine.dooktravel.R;
import com.softmine.dooktravel.adapter.AdapterSearchResult;
import com.softmine.dooktravel.pojos.CategoryList;
import com.softmine.dooktravel.pojos.CountryList;
import com.softmine.dooktravel.pojos.Model;
import com.softmine.dooktravel.pojos.ProfileList;
import com.softmine.dooktravel.pojos.ProfileListRequest;
import com.softmine.dooktravel.pojos.StateList;
import com.softmine.dooktravel.serviceconnection.CompleteListener;
import com.softmine.dooktravel.serviceconnection.ServiceConnection;
import com.softmine.dooktravel.util.C;
import com.softmine.dooktravel.util.SharedPreference;
import com.softmine.dooktravel.util.Utils;
import com.softmine.dooktravel.validations.ValidateEditText;
import com.softmine.dooktravel.validations.Validations;
import com.softmine.dooktravel.view.ActivityHome;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSearchResult extends Fragment implements CompleteListener{

    private RecyclerView recyclerView;
    private AdapterSearchResult adapterSearchResult;
    private List<Model> modelList;
    Spinner spnCountry, spnState, spnCategory;
    Button btnGo;
    int flags;
    String action;
    Validations validation ;
    TextView tvSearchResult;
    LinearLayoutManager mLayoutManager;
    ValidateEditText etKeyword;
    CategoryList categoryList;
    CountryList countryList;
    StateList stateList;
    int mCountryPos=-1,mStatePos=-1,mCategoryPos=-1;
    ProfileListRequest profileListRequest;
    Utils utils;
    public FragmentSearchResult() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        validation = new Validations();
        utils=new Utils();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        btnGo = (Button) view.findViewById(R.id.search_go_btn);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        etKeyword=new ValidateEditText((EditText)view.findViewById(R.id.etkeyword),getActivity(),flags);
        btnGo.setOnClickListener(mGoClickListner);
        spnCountry = (Spinner) view.findViewById(R.id.spinner_country);
        spnState = (Spinner) view.findViewById(R.id.spinner_state);
        spnCategory = (Spinner) view.findViewById(R.id.spinner_filter_category);
        tvSearchResult=(TextView)view.findViewById(R.id.tvSearchResult) ;
      /*  ArrayAdapter countryAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.country, R.layout.spinner_item);

        countryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spnCountry.setAdapter(countryAdapter);*/
        spnCountry.setOnItemSelectedListener(mOnCountrySelectedListner);
        spnState.setOnItemSelectedListener(mOnStateItemSelectedListner);
        spnCategory.setOnItemSelectedListener(mOnCategorySelectedListner);
        profileListRequest=new ProfileListRequest();
/*
        ArrayAdapter stateAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.state, R.layout.spinner_item);
        stateAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spnState.setAdapter(stateAdapter);*/

        /*ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.category, R.layout.spinner_item);
        categoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spnCategory.setAdapter(categoryAdapter);*/

        btnGo.setTypeface(Utils.getSemiBoldTypeFace(getActivity()));
        tvSearchResult.setTypeface(Utils.getRegularTypeFace(getActivity()));
        validation.addtoList(etKeyword);
        getCategoryList();
    }
    @Override
    public void onResume() {
        super.onResume();

        ActivityHome.tvEdit.setVisibility(View.GONE);

    }
    AdapterView.OnItemSelectedListener mOnStateItemSelectedListner=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(stateList!=null && position!=0&& position!=mStatePos) {
            profileListRequest.setStateId(stateList.getState().get(position-1).getId());
            mStatePos=position;

            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    AdapterView.OnItemSelectedListener mOnCategorySelectedListner=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(categoryList!=null && position!=0&& position!=mCategoryPos) {
                profileListRequest.setCategoryId(categoryList.getCategory().get(position - 1).getCategoryId());
                mCategoryPos=position;


            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    AdapterView.OnItemSelectedListener mOnCountrySelectedListner=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if(countryList!=null && position!=0&& position!=mCountryPos) {
                mCountryPos=position;
                profileListRequest.setCountryId(countryList.getCountry().get(position - 1).getId());
                getStateList(countryList.getCountry().get(position - 1).getId());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
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
        serviceConnection.makeJsonObjectRequest(C.STATE_METHOD,jsonBody,FragmentSearchResult.this);

    }
    void  getCategoryList(){
        action= C.CATEGORY_LIST_METHOD;
        ServiceConnection serviceConnection=new ServiceConnection();
        serviceConnection.makeJsonObjectRequest(C.CATEGORY_LIST_METHOD,null,FragmentSearchResult.this);
    }
    void getCountryList(){
        action=C.COUNTRY_METHOD;
        ServiceConnection serviceConnection=new ServiceConnection();
        serviceConnection.serviceRequest(C.COUNTRY_METHOD,null,FragmentSearchResult.this);
    }
    View.OnClickListener mGoClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(validation.validateAllEditText()){
                    Utils.hideSoftKeyboard(getActivity());
                if(utils.isInternetOn(getActivity())) {
                    profileListRequest.setMemberId(SharedPreference.getInstance(getActivity()).getString(C.MEMBER_ID));
                    profileListRequest.setToken(SharedPreference.getInstance(getActivity()).getString(C.TOKEN));
                    profileListRequest.setKeyword(etKeyword.getEditText().getText().toString());

                    Gson gson = new Gson();
                    String obj = gson.toJson(profileListRequest);
                    try {
                        action = C.PROFILE_LIST_METHOD;
                        JSONObject jsonObject = new JSONObject(obj);
                        ServiceConnection serviceConnection = new ServiceConnection();
                        serviceConnection.makeJsonObjectRequest(C.PROFILE_LIST_METHOD, jsonObject, FragmentSearchResult.this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    getDailogConfirm(getString(R.string.internet_issue)
                            , "Internet Issue");
                }
            }

        }
    };

    boolean isAllValid(){

        if(spnCountry.getSelectedItem().toString().equals(C.SELECT_COUNTRY)){
            Utils.showToast(getActivity(),getString(R.string.select_country));

            return false;
        }

        else if(spnState.getSelectedItem().toString().equals(C.SELECT_STATE)){

            Utils.showToast(getActivity(),getString(R.string.select_state));
            return false;
        }
        else if(spnCategory.getSelectedItem().toString().equals(C.SELECT_CATEGORY)){
            Utils.showToast(getActivity(),getString(R.string.select_catogary));
            return false;
        }
        return true;
    }
    private ArrayList<Model> getDataSet() {
        ArrayList results = new ArrayList<Model>();
        for (int index = 0; index < 6; index++) {
            Model obj = new Model("Sandeep Gupta", "Dook Travels pvt Ltd",
                    "New Delhi, India");
            results.add(index, obj);
        }
        return results;
    }

    @Override
    public void done(String response) {

        try {
            Log.e(FragmentSearchResult.class.getName(), "RESPONSE==" + response);

            if (action.equals(C.CATEGORY_LIST_METHOD)) {
                Gson gson = new Gson();
                categoryList = gson.fromJson(response, CategoryList.class);
                if (!categoryList.getError()) {
                    String[] catArr = new String[categoryList.getCategory().size() + 1];
                    catArr[0] = C.SELECT_CATEGORY;
                    for (int i = 0; i < categoryList.getCategory().size(); i++) {

                        catArr[i + 1] = String.valueOf(categoryList.getCategory().get(i).getCategoryName());

                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, catArr); //selected item will look like a spinner set from XML

                    spnCategory.setAdapter(spinnerArrayAdapter);
                    spinnerArrayAdapter.notifyDataSetChanged();
                    getCountryList();

                } else {
                    String[] countArr = new String[1];
                    countArr[0] = C.SELECT_CATEGORY;
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, countArr); //selected item will look like a spinner set from XML
                    spnCategory.setAdapter(spinnerArrayAdapter);
                    spinnerArrayAdapter.notifyDataSetChanged();

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

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, countArr); //selected item will look like a spinner set from XML

                    spnCountry.setAdapter(spinnerArrayAdapter);
                    spinnerArrayAdapter.notifyDataSetChanged();


                } else {
                    String[] countArr = new String[1];
                    countArr[0] = C.SELECT_COUNTRY;
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, countArr); //selected item will look like a spinner set from XML
                    spnCountry.setAdapter(spinnerArrayAdapter);
                    spinnerArrayAdapter.notifyDataSetChanged();

                }
            } else if (action.equals(C.STATE_METHOD)) {
                Gson gson = new Gson();
                stateList = gson.fromJson(response, StateList.class);
                if (!stateList.getError()) {
                    String[] stateArr = new String[stateList.getState().size() + 1];
                    stateArr[0] = C.STATE_METHOD;
                    for (int i = 0; i < stateList.getState().size(); i++) {
                        stateArr[i + 1] = String.valueOf(stateList.getState().get(i).getName());
                    }


                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stateArr); //selected item will look like a spinner set from XML
                    spnState.setAdapter(spinnerArrayAdapter);
                    spinnerArrayAdapter.notifyDataSetChanged();

                } else {
                    String[] stateArr = new String[1];
                    stateArr[0] = C.STATE_METHOD;
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stateArr); //selected item will look like a spinner set from XML
                    spnState.setAdapter(spinnerArrayAdapter);
                    spinnerArrayAdapter.notifyDataSetChanged();

                }
            } else if (action.equals(C.PROFILE_LIST_METHOD)) {
                Gson gson = new Gson();
                ProfileList profileList = gson.fromJson(response, ProfileList.class);
                if (!profileList.getError()) {
                    adapterSearchResult = new AdapterSearchResult(profileList.getMember(), getActivity());
                    recyclerView.setAdapter(adapterSearchResult);
                } else {
                    Utils.showToast(getActivity(), profileList.getMessage());
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
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
    @Override
    public Context getApplicationsContext() {
        return getActivity();
    }
}
