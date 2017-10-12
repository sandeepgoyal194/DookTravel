package com.softmine.dooktravel.presenter;

import android.content.Context;

import com.softmine.dooktravel.model.ILoginResponseIntractor;
import com.softmine.dooktravel.model.LoginResponseIntractorImpl;
import com.softmine.dooktravel.view.fragments.IFragmentView;

import org.json.JSONObject;

/**
 * Created by GAURAV on 7/31/2017.
 */

public class FragmentLoginPresenterImpl implements IFragmentLoginPresenter,ILoginResponseIntractor.OnLoginFinishedListener {
    IFragmentView mView;
    Context context;
        private ILoginResponseIntractor iLoginResponseIntractor;
    public FragmentLoginPresenterImpl(IFragmentView mView, Context context) {
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
    public void validateLoginSocail(JSONObject jsonObject) {
        if(mView!=null){
            mView.showProgress();
        }
        iLoginResponseIntractor.getLoginResponseSocail(jsonObject, this);
    }

    @Override
    public void onDestroy() {
        try {
            mView = null;
        }
        catch (Exception e){
            e.printStackTrace();
        }
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
