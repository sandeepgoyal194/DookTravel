package com.softmine.dooktravel.view.fragments;

/**
 * Created by GAURAV on 8/1/2017.
 */

public interface IFragmentLoginView {
    public void getLoginResponseSuccess(String response);
    public void getLoginResponseError(String response);
    void showProgress();

    void hideProgress();
}
