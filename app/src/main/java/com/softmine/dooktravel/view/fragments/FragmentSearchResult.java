package com.softmine.dooktravel.view.fragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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
import com.softmine.dooktravel.presenter.FragmentSearchResultPresenterImpl;
import com.softmine.dooktravel.presenter.IFragmentSearchResultPresenter;
import com.softmine.dooktravel.util.C;
import com.softmine.dooktravel.util.SharedPreference;
import com.softmine.dooktravel.util.Utils;
import com.softmine.dooktravel.validations.ValidateEditText;
import com.softmine.dooktravel.validations.Validations;
import com.softmine.dooktravel.view.ActivityHome;
import com.softmine.dooktravel.view.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSearchResult extends Fragment implements IFragmentView{

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
    ArrayAdapter<String> spinnerCategoryAdapter=null;
    ArrayAdapter<String> spinnerStateAdapter=null;
    ArrayAdapter<String> spinnerCountryAdapter=null;

    ArrayAdapter<String> spinnerCityAdapter=null;
    IFragmentSearchResultPresenter mIFragmentSearchResultPresenter;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validation = new Validations();
        utils=new Utils();
        mIFragmentSearchResultPresenter=new FragmentSearchResultPresenterImpl(this,getActivity());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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


        String[] catArr = new String[1];
        catArr[0] = C.SELECT_CATEGORY;
                /*    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stateArr); //selected item will look like a spinner set from XML
                    spnState.setAdapter(spinnerArrayAdapter);
                    spinnerArrayAdapter.notifyDataSetChanged();*/
        setCategoryList(Arrays.asList(catArr));

        String[] stateArr = new String[1];
        stateArr[0] = C.SELECT_STATE;
                /*    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stateArr); //selected item will look like a spinner set from XML
                    spnState.setAdapter(spinnerArrayAdapter);
                    spinnerArrayAdapter.notifyDataSetChanged();*/
        setStateList(Arrays.asList(stateArr));

        String[] counArr = new String[1];
        counArr[0] = C.SELECT_COUNTRY;
                /*    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stateArr); //selected item will look like a spinner set from XML
                    spnState.setAdapter(spinnerArrayAdapter);
                    spinnerArrayAdapter.notifyDataSetChanged();*/
        setCountryList(Arrays.asList(counArr));

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
        mIFragmentSearchResultPresenter.getStateList(jsonBody,true);

    }
    void  getCategoryList(){
        action= C.CATEGORY_LIST_METHOD;
        mIFragmentSearchResultPresenter.getCategoryList(null,true);

    }
    void getCountryList(){
        action=C.COUNTRY_METHOD;
        mIFragmentSearchResultPresenter.getCountryList(null,false);

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
                        mIFragmentSearchResultPresenter.getProfileList(jsonObject);

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

    void setCategoryList( List<String> catList){
        try {

            spinnerCategoryAdapter = new ArrayAdapter<String>(
                    getActivity(), R.layout.spinner_item_black, catList) {
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
                    getActivity(), R.layout.spinner_item_black, countryList) {
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
                    getActivity(), R.layout.spinner_item_black, StateList) {
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
            spnState.setAdapter(spinnerStateAdapter);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void getResponseSuccess(String response) {

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

                  /*  ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, catArr); //selected item will look like a spinner set from XML

                    spnCategory.setAdapter(spinnerArrayAdapter);*/

                    setCategoryList(Arrays.asList(catArr));
                    spinnerCategoryAdapter.notifyDataSetChanged();
                    getCountryList();

                } else {
                    String[] countArr = new String[1];
                    countArr[0] = C.SELECT_CATEGORY;
                    /*ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, countArr); //selected item will look like a spinner set from XML
                    spnCategory.setAdapter(spinnerArrayAdapter);*/
                    setCategoryList(Arrays.asList(countArr));
                    spinnerCategoryAdapter.notifyDataSetChanged();
                    //   spinnerArrayAdapter.notifyDataSetChanged();

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



                } else {
                    String[] countArr = new String[1];
                    countArr[0] = C.SELECT_COUNTRY;
                   /* ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, countArr); //selected item will look like a spinner set from XML
                    spnCountry.setAdapter(spinnerArrayAdapter);
                    spinnerArrayAdapter.notifyDataSetChanged();*/
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


                  /*  ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stateArr); //selected item will look like a spinner set from XML
                    spnState.setAdapter(spinnerArrayAdapter);
                    spinnerArrayAdapter.notifyDataSetChanged();*/
                    setStateList(Arrays.asList(stateArr));
                    spinnerStateAdapter.notifyDataSetChanged();

                } else {
                    String[] stateArr = new String[1];
                    stateArr[0] = C.SELECT_STATE;
                /*    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stateArr); //selected item will look like a spinner set from XML
                    spnState.setAdapter(spinnerArrayAdapter);
                    spinnerArrayAdapter.notifyDataSetChanged();*/
                    setStateList(Arrays.asList(stateArr));
                    spinnerStateAdapter.notifyDataSetChanged();

                }
            } else if (action.equals(C.PROFILE_LIST_METHOD)) {
                Gson gson = new Gson();
                ProfileList profileList = gson.fromJson(response, ProfileList.class);
                if (!profileList.getError()) {
                    adapterSearchResult = new AdapterSearchResult(profileList.getMember(), getActivity());
                    recyclerView.setAdapter(adapterSearchResult);
                } else {
                    if(profileList.getMessage().equals(C.InvalidToken)){
                        Utils.showToast(getActivity(), C.Session_expired);
                        Intent intent=new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra(C.SCREEN,C.FRAGMENT_LOGIN);
                        startActivity(intent);
                    }
                    else {
                        Utils.showToast(getActivity(), profileList.getMessage());
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
