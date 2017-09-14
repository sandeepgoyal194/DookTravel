package com.softmine.dooktravel.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gaurav.garg on 18-07-2017.
 */

public class RegisterStatus implements Serializable {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("OTP")
    @Expose
    private String OTP;

    /**
     * No args constructor for use in serialization
     *
     */
    public RegisterStatus() {
    }

    /**
     *
     * @param message
     * @param error
     * @param OTP
     */

    public RegisterStatus(Boolean error, String message, String OTP) {
        this.error = error;
        this.message = message;
        this.OTP = OTP;
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

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }
}
