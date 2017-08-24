package com.softmine.dooktravel.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.softmine.dooktravel.model.Profile;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gaurav.garg on 18-07-2017.
 */

public class ProfileStatus implements Serializable {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("member")
    @Expose
    private List<Profile> member = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public ProfileStatus() {
    }

    /**
     *
     * @param member
     * @param message
     * @param error
     */
    public ProfileStatus(Boolean error, String message, List<Profile> member) {
        super();
        this.error = error;
        this.message = message;
        this.member = member;
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

    public List<Profile> getMember() {
        return member;
    }

    public void setMember(List<Profile> member) {
        this.member = member;
    }
}
