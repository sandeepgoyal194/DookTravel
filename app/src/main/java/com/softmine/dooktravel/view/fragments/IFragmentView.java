package com.softmine.dooktravel.view.fragments;

/**
 * Created by GAURAV on 8/1/2017.
 */

public interface IFragmentView {
    public void getResponseSuccess(String response);
    public void getResponseError(String response);
    void showProgress();

    void hideProgress();
}
