package com.softmine.dooktravel.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.softmine.dooktravel.model.Profile;

import java.io.Serializable;
import java.util.List;

/**
 * Created by GAURAV on 7/20/2017.
 */

public class ProfileList implements Serializable{

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
    public ProfileList() {
    }

    /**
     *
     * @param member
     * @param message
     * @param error
     */
    public ProfileList(Boolean error, String message, List<Profile> member) {
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
