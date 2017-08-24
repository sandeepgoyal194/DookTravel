package com.softmine.dooktravel.presenter;

import org.json.JSONObject;

/**
 * Created by GAURAV on 8/23/2017.
 */

public interface IFragmentChangePasswordPresenter {
        void validateChangePassword(JSONObject jsonObject);
        void onDestroy();
}
