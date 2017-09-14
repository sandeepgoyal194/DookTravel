package com.softmine.dooktravel.presenter;

import org.json.JSONObject;

/**
 * Created by GAURAV on 9/14/2017.
 */

public interface IFragmentOtpPresenter {
    void resend(JSONObject jsonObject);
    void onDestroy();
}
