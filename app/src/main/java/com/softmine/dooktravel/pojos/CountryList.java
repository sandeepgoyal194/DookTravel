package com.softmine.dooktravel.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by GAURAV on 7/16/2017.
 */

public class CountryList implements Serializable {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("country")
    @Expose
    private List<Country> country = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public CountryList() {
    }

    /**
     *
     * @param message
     * @param error
     * @param country
     */
    public CountryList(Boolean error, String message, List<Country> country) {
        super();
        this.error = error;
        this.message = message;
        this.country = country;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Country> getCountry() {
        return country;
    }

    public void setCountry(List<Country> country) {
        this.country = country;
    }
}
