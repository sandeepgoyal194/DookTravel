package com.softmine.dooktravel.presenter;

import org.json.JSONObject;

/**
 * Created by GAURAV on 8/25/2017.
 */

public interface IFragmentSearchResultPresenter {
    void getProfileList(JSONObject jsonObject);
    void getCategoryList(JSONObject jsonObject,boolean isShowProgressbar);
    void getCountryList(JSONObject jsonObject,boolean isShowProgressbar);
    void getStateList(JSONObject jsonObject,boolean isShowProgressbar);
    void getCityList(JSONObject jsonObject,boolean isShowProgressbar);

    void onDestroy();
}
