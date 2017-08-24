package com.softmine.dooktravel.presenter;

import android.content.Context;

import com.softmine.dooktravel.model.CategoryListIntractorImpl;
import com.softmine.dooktravel.model.CityListIntractorImpl;
import com.softmine.dooktravel.model.CountryListIntractorImpl;
import com.softmine.dooktravel.model.ICategoryListIntractor;
import com.softmine.dooktravel.model.ICityListIntractor;
import com.softmine.dooktravel.model.ICountryListIntractor;
import com.softmine.dooktravel.model.IProfileListIntractor;
import com.softmine.dooktravel.model.IStateListIntractor;
import com.softmine.dooktravel.model.ProfileListIntractorImpl;
import com.softmine.dooktravel.model.StateListIntractorImpl;
import com.softmine.dooktravel.view.fragments.IFragmentView;

import org.json.JSONObject;

/**
 * Created by GAURAV on 8/25/2017.
 */

public class FragmentSearchResultPresenterImpl implements
        IFragmentSearchResultPresenter,IProfileListIntractor.OnGetProfileListFinishedListener,
        ICategoryListIntractor.OnGetCategoryListFinishedListener,ICountryListIntractor.OnGetCountryListFinishedListener,
        IStateListIntractor.OnGetStateListFinishedListener,ICityListIntractor.OnGetCityListFinishedListener {

    IFragmentView mView;
    Context context;
    private IProfileListIntractor iProfileListIntractor;
    private ICategoryListIntractor iCategoryListIntractor;
    private ICountryListIntractor iCountryListIntractor;
    private IStateListIntractor iStateListIntractor;
    private ICityListIntractor iCityListIntractor;


    public FragmentSearchResultPresenterImpl(IFragmentView mView, Context context) {
        this.mView = mView;
        this.context = context;
        iProfileListIntractor=new ProfileListIntractorImpl();
        iCategoryListIntractor=new CategoryListIntractorImpl();
        iCountryListIntractor=new CountryListIntractorImpl();
        iStateListIntractor=new StateListIntractorImpl();
        iCityListIntractor=new CityListIntractorImpl();
    }

    @Override
    public void getProfileList(JSONObject jsonObject) {
        if(mView!=null){
            mView.showProgress();
        }
        iProfileListIntractor.getProfileListResponse(jsonObject, this);
    }

    @Override
    public void getCategoryList(JSONObject jsonObject,boolean isShowProgressbar) {
        if(mView!=null && isShowProgressbar){
            mView.showProgress();
        }
        iCategoryListIntractor.getCategoryListResponse(jsonObject, this);
    }

    @Override
    public void getCountryList(JSONObject jsonObject,boolean isShowProgressbar) {
        if(mView!=null && isShowProgressbar){
            mView.showProgress();
        }
        iCountryListIntractor.getCountryListResponse(jsonObject, this);
    }

    @Override
    public void getStateList(JSONObject jsonObject,boolean isShowProgressbar) {
        if(mView!=null && isShowProgressbar){
            mView.showProgress();
        }
        iStateListIntractor.getStateListResponse(jsonObject, this);
    }

    @Override
    public void getCityList(JSONObject jsonObject,boolean isShowProgressbar) {
        if(mView!=null && isShowProgressbar){
            mView.showProgress();
        }
        iCityListIntractor.getCityListResponse(jsonObject, this);
    }

    @Override
    public void onDestroy() {
        mView=null;
    }

    @Override
    public void onSuccess(String response) {
        if(mView!=null){
            mView.hideProgress();
            mView.getResponseSuccess(response);
        }
    }

    @Override
    public void onError(String response) {
        if(mView!=null){
            mView.hideProgress();
            mView.getResponseError(response);
        }
    }

    @Override
    public void onGetCategoryListSuccess(String response) {
        if(mView!=null){
            mView.hideProgress();
            mView.getResponseSuccess(response);
        }
    }

    @Override
    public void onGetCategoryListError(String response) {

    }

    @Override
    public void onGetCountryListSuccess(String response) {
        if(mView!=null){
            mView.hideProgress();
            mView.getResponseSuccess(response);
        }
    }

    @Override
    public void onGetCountryListError(String response) {

    }

    @Override
    public void onGetStateListSuccess(String response) {
        if(mView!=null){
            mView.hideProgress();
            mView.getResponseSuccess(response);
        }
    }

    @Override
    public void onGetStateListError(String response) {

    }

    @Override
    public void onGetCityListSuccess(String response) {
        if(mView!=null){
            mView.hideProgress();
            mView.getResponseSuccess(response);
        }
    }

    @Override
    public void onGetCityListError(String response) {

    }

    @Override
    public Context getAPPContext() {
        return context;
    }

}
