package com.softmine.dooktravel.presenter;

import org.json.JSONObject;

/**
 * Created by GAURAV on 8/24/2017.
 */

public interface IFragmentProfessionalDetailPresenter {
    void registerDetail(JSONObject jsonObject);
    void updateDetail(JSONObject jsonObject);
    void getCategoryList(JSONObject jsonObject,boolean isShowProgressbar);
    void getCountryList(JSONObject jsonObject,boolean isShowProgressbar);
    void getStateList(JSONObject jsonObject,boolean isShowProgressbar);
    void getCityList(JSONObject jsonObject,boolean isShowProgressbar);

    void onDestroy();
}
