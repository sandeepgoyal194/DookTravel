package com.softmine.dooktravel.presenter;

import org.json.JSONObject;

/**
 * Created by GAURAV on 8/25/2017.
 */

public interface IActivityHomePresenter {
    void logout(JSONObject jsonObject);
    void getProfileInfo(JSONObject jsonObject,boolean isShowProgressbar);
}
