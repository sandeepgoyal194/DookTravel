package com.softmine.dooktravel.presenter;

import android.content.Context;

import com.softmine.dooktravel.model.ILoginResponseIntractor;
import com.softmine.dooktravel.model.LoginResponseIntractorImpl;
import com.softmine.dooktravel.view.fragments.IFragmentLoginView;

import org.json.JSONObject;

/**
 * Created by GAURAV on 7/31/2017.
 */

public class FragmentLoginPresenterImpl implements IFragmentLoginPresenter,ILoginResponseIntractor.OnLoginFinishedListener {
    IFragmentLoginView mView;
    Context context;
    private ILoginResponseIntractor iLoginResponseIntractor;
    public FragmentLoginPresenterImpl(IFragmentLoginView mView,Context context) {
        this.mView = mView;
        this.context=context;
        iLoginResponseIntractor=new LoginResponseIntractorImpl();
    }


    @Override
    public void validateLogin(JSONObject jsonObject) {
            if(mView!=null){
                mView.showProgress();
            }
        iLoginResponseIntractor.getLoginResponse(jsonObject, this);

    }

    @Override
    public void onDestroy() {
        mView=null;
    }

    @Override
    public void onSuccess(String response) {
        if(mView!=null){
            mView.hideProgress();
            mView.getLoginResponseSuccess(response);
        }
    }

    @Override
    public void onError(String response) {
        if(mView!=null){
            mView.hideProgress();
            mView.getLoginResponseError(response);
        }
    }

    @Override
    public Context getAPPContext() {
        return context;
    }
}
