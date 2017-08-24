package com.softmine.dooktravel.presenter;

import android.content.Context;

import com.softmine.dooktravel.model.ChangePasswordResponseIntractorImpl;
import com.softmine.dooktravel.model.IChangePasswordResponseIntractor;
import com.softmine.dooktravel.view.fragments.IFragmentView;

import org.json.JSONObject;

/**
 * Created by GAURAV on 8/23/2017.
 */

public class FragmentChangePasswordPresenterImpl implements IFragmentChangePasswordPresenter, IChangePasswordResponseIntractor.OnChangePasswordResponseFinishedListener {
    IFragmentView mView;
    Context context;
    IChangePasswordResponseIntractor iChangePasswordResponseIntractor;
    public FragmentChangePasswordPresenterImpl(IFragmentView mView, Context context) {
        this.mView = mView;
        this.context = context;
        iChangePasswordResponseIntractor=new ChangePasswordResponseIntractorImpl();
    }

    @Override
    public void validateChangePassword(JSONObject jsonObject) {
        if(mView!=null){
            mView.showProgress();
        }
        iChangePasswordResponseIntractor.getChangePasswordResponse(jsonObject,this);
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
    public Context getAPPContext() {
        return context;
    }
}
