package com.softmine.dooktravel.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by GAURAV on 7/16/2017.
 */

public class Member implements Serializable{

    @SerializedName("member_id")
    @Expose
    private String memberId;
    @SerializedName("email_id")
    @Expose
    private String emailId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("middle_name")
    @Expose
    private Object middleName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("token")
    @Expose
    private String token;

    /**
     * No args constructor for use in serialization
     *
     */
    public Member() {
    }

    /**
     *
     * @param middleName
     * @param lastName
     * @param emailId
     * @param token
     * @param memberId
     * @param firstName
     */
    public Member(String memberId, String emailId, String firstName, Object middleName, String lastName, String token) {
        super();
        this.memberId = memberId;
        this.emailId = emailId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.token = token;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Object getMiddleName() {
        return middleName;
    }

    public void setMiddleName(Object middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
