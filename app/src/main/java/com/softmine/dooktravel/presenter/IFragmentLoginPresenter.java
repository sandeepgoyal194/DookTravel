package com.softmine.dooktravel.presenter;

import org.json.JSONObject;

/**
 * Created by GAURAV on 8/1/2017.
 */

public interface IFragmentLoginPresenter  {

    void validateLogin(JSONObject jsonObject);
    void validateLoginSocail(JSONObject jsonObject);

    void onDestroy();
}
