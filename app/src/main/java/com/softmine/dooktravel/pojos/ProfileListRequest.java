package com.softmine.dooktravel.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by GAURAV on 7/20/2017.
 */

public class ProfileListRequest implements Serializable {

    @SerializedName("member_id")
    @Expose
    private String memberId;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("keyword")
    @Expose
    private String keyword;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("city_id")
    @Expose
    private String cityId;

    /**
     * No args constructor for use in serialization
     *
     */
    public ProfileListRequest() {
    }

    /**
     *
     * @param countryId
     * @param cityId
     * @param token
     * @param stateId
     * @param categoryId
     * @param keyword
     * @param memberId
     */
    public ProfileListRequest(String memberId, String token, String keyword, String categoryId, String countryId, String stateId, String cityId) {
        super();
        this.memberId = memberId;
        this.token = token;
        this.keyword = keyword;
        this.categoryId = categoryId;
        this.countryId = countryId;
        this.stateId = stateId;
        this.cityId = cityId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}
