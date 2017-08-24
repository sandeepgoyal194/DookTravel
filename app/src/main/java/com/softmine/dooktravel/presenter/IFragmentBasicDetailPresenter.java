package com.softmine.dooktravel.presenter;

import org.json.JSONObject;

/**
 * Created by GAURAV on 8/23/2017.
 */

public interface IFragmentBasicDetailPresenter {
    void getBasicDetail(JSONObject jsonObject);
    void onDestroy();
}
