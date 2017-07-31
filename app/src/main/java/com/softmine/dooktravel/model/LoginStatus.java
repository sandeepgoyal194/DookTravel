package com.softmine.dooktravel.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.softmine.dooktravel.pojos.Member;

import java.io.Serializable;

/**
 * Created by GAURAV on 7/16/2017.
 */

public class LoginStatus implements Serializable {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("member")
    @Expose
    private Member member;

    /**
     * No args constructor for use in serialization
     *
     */
    public LoginStatus() {
    }

    /**
     *
     * @param member
     * @param message
     * @param error
     */
    public LoginStatus(Boolean error, String message, Member member) {
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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
