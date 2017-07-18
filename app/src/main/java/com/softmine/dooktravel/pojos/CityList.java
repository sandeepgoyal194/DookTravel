package com.softmine.dooktravel.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by GAURAV on 7/16/2017.
 */

public class CityList implements Serializable{

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("city")
    @Expose
    private List<City> city = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public CityList() {
    }

    /**
     *
     * @param message
     * @param error
     * @param city
     */
    public CityList(Boolean error, String message, List<City> city) {
        super();
        this.error = error;
        this.message = message;
        this.city = city;
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

    public List<City> getCity() {
        return city;
    }

    public void setCity(List<City> city) {
        this.city = city;
    }
}
