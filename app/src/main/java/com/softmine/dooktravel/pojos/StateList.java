package com.softmine.dooktravel.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by GAURAV on 7/16/2017.
 */

public class StateList implements Serializable {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("state")
    @Expose
    private List<State> state = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public StateList() {
    }

    /**
     *
     * @param message
     * @param error
     * @param state
     */
    public StateList(Boolean error, String message, List<State> state) {
        super();
        this.error = error;
        this.message = message;
        this.state = state;
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

    public List<State> getState() {
        return state;
    }

    public void setState(List<State> state) {
        this.state = state;
    }

}
