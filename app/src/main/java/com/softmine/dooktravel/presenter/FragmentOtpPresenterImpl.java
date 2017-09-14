package com.softmine.dooktravel.presenter;

import android.content.Context;

import com.softmine.dooktravel.model.IOTPResendIntractor;
import com.softmine.dooktravel.model.OTPResendIntractorImpl;
import com.softmine.dooktravel.view.fragments.IFragmentView;

import org.json.JSONObject;

/**
 * Created by GAURAV on 9/14/2017.
 */

public class FragmentOtpPresenterImpl implements IFragmentOtpPresenter , IOTPResendIntractor.OnOTPResendFinishedListener {

    IFragmentView mView;
    Context context;
    private IOTPResendIntractor iotpResendIntractor;
    public FragmentOtpPresenterImpl(IFragmentView mView, Context context) {
        this.mView = mView;
        this.context=context;
        iotpResendIntractor=new OTPResendIntractorImpl();
    }

    @Override
    public void resend(JSONObject jsonObject) {
        if(mView!=null){
            mView.showProgress();
        }
        iotpResendIntractor.resend(jsonObject, this);
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
