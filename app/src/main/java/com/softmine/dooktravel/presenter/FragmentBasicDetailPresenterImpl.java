package com.softmine.dooktravel.presenter;

import android.content.Context;

import com.softmine.dooktravel.model.BasicDetailResponseIntractorImpl;
import com.softmine.dooktravel.model.IBasicDetailResponseIntractor;
import com.softmine.dooktravel.view.fragments.IFragmentView;

import org.json.JSONObject;

/**
 * Created by GAURAV on 8/23/2017.
 */

public class FragmentBasicDetailPresenterImpl implements IFragmentBasicDetailPresenter , IBasicDetailResponseIntractor.OnBasicDetailResponseFinishedListener{

    IFragmentView mView;
    Context context;
    IBasicDetailResponseIntractor iBasicDetailResponseIntractor;

    public FragmentBasicDetailPresenterImpl(IFragmentView mView, Context context) {
        this.mView = mView;
        this.context = context;
        iBasicDetailResponseIntractor=new BasicDetailResponseIntractorImpl();
    }

    @Override
    public void getBasicDetail(JSONObject jsonObject) {
        if(mView!=null){
            mView.showProgress();
        }
        iBasicDetailResponseIntractor.getBasicDetail(jsonObject,this);
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
