package com.softmine.dooktravel.presenter;

import android.content.Context;

import com.softmine.dooktravel.model.BasicDetailResponseIntractorImpl;
import com.softmine.dooktravel.model.IBasicDetailResponseIntractor;
import com.softmine.dooktravel.model.ILogoutIntractor;
import com.softmine.dooktravel.model.LogoutIntractorImpl;
import com.softmine.dooktravel.view.fragments.IFragmentView;

import org.json.JSONObject;

/**
 * Created by GAURAV on 8/25/2017.
 */

public class ActivityHomePresenterImpl implements IActivityHomePresenter , ILogoutIntractor.OnLogoutFinishedListener, IBasicDetailResponseIntractor.OnBasicDetailResponseFinishedListener{

    IFragmentView mView;
    Context context;
    IBasicDetailResponseIntractor iBasicDetailResponseIntractor;
    ILogoutIntractor iLogoutIntractor;

    public ActivityHomePresenterImpl(IFragmentView mView, Context context) {
        this.mView = mView;
        this.context = context;
        iBasicDetailResponseIntractor=new BasicDetailResponseIntractorImpl();
        iLogoutIntractor=new LogoutIntractorImpl();
    }

    @Override
    public void logout(JSONObject jsonObject) {
        if(mView!=null){
            mView.showProgress();
        }
        iLogoutIntractor.logout(jsonObject,this);
    }

    @Override
    public void getProfileInfo(JSONObject jsonObject, boolean isShowProgressbar) {
        if(mView!=null && isShowProgressbar){
            mView.showProgress();
        }
        iBasicDetailResponseIntractor.getBasicDetail(jsonObject, this);
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
    public Context getAPPContext() {
        return context;
    }
}
