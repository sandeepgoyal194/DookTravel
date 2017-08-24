package com.softmine.dooktravel.presenter;

import android.content.Context;

import com.softmine.dooktravel.model.ISignUpResponseIntractor;
import com.softmine.dooktravel.model.SignUpResponseIntractorImpl;
import com.softmine.dooktravel.view.fragments.IFragmentView;

import org.json.JSONObject;

/**
 * Created by GAURAV on 8/23/2017.
 */

public class FragmentSignUpPresenterImpl implements IFragmentSignUpPresenter, ISignUpResponseIntractor.OnSignUpFinishedListener {
    IFragmentView mView;
    Context context;
    ISignUpResponseIntractor iSignUpResponseIntractor;

    public FragmentSignUpPresenterImpl(IFragmentView mView, Context context) {
        this.mView = mView;
        this.context = context;
        iSignUpResponseIntractor=new SignUpResponseIntractorImpl();
    }

    @Override
    public void validateSignUp(JSONObject jsonObject) {
        if(mView!=null){
            mView.showProgress();
        }
        iSignUpResponseIntractor.getSignUpResponse(jsonObject,this);
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
