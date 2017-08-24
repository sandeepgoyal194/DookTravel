package com.softmine.dooktravel.presenter;

import android.content.Context;

import com.softmine.dooktravel.model.IForgotPasswordResponseIntractor;
import com.softmine.dooktravel.model.ForgotPasswordResponseIntractorImpl;
import com.softmine.dooktravel.view.fragments.IFragmentView;

import org.json.JSONObject;

/**
 * Created by GAURAV on 8/23/2017.
 */

public class FragmentForgotPasswordPresenterImpl implements IFragmentForgotPasswordPresenter, IForgotPasswordResponseIntractor.OnForgotPasswordResponseFinishedListener{

    IFragmentView mView;
    Context context;
    IForgotPasswordResponseIntractor mIForgotPasswordResponseIntractor;
    public FragmentForgotPasswordPresenterImpl(IFragmentView mView, Context context) {
        this.mView = mView;
        this.context = context;
        mIForgotPasswordResponseIntractor=new ForgotPasswordResponseIntractorImpl();
    }

    @Override
    public void validateForgotPassword(JSONObject jsonObject) {
        if(mView!=null){
            mView.showProgress();
        }
        mIForgotPasswordResponseIntractor.getForgotPasswordResponse(jsonObject,this);
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
