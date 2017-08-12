package com.softmine.dooktravel.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by GAURAV on 7/16/2017.
 */

public class ProfileDetail implements Serializable{

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("socialid")
    @Expose
    private String socialid;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("categoryid")
    @Expose
    private String categoryid;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("middlename")
    @Expose
    private String middlename;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("picturename")
    @Expose
    private String picturename;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("marital")
    @Expose
    private String marital;
    @SerializedName("organization")
    @Expose
    private String organization;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("working")
    @Expose
    private String working;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("countryid")
    @Expose
    private String countryid;
    @SerializedName("stateid")
    @Expose
    private String stateid;
    @SerializedName("cityid")
    @Expose
    private String cityid;
    @SerializedName("zip")
    @Expose
    private String zip;
    @SerializedName("skype")
    @Expose
    private String skype;
    @SerializedName("secondaryEmail")
    @Expose
    private String secondaryEmail;
    @SerializedName("mobile1")
    @Expose
    private String mobile1;
    @SerializedName("mobile2")
    @Expose
    private String mobile2;
    @SerializedName("about")
    @Expose
    private String about;

    /**
     * No args constructor for use in serialization
     *
     */
    public ProfileDetail() {
    }

    /**
     *
     * @param stateid
     * @param zip
     * @param phone
     * @param middlename
     * @param mobile2
     * @param about
     * @param lastname
     * @param firstname
     * @param password
     * @param countryid
     * @param secondaryEmail
     * @param cityid
     * @param picture
     * @param picturename
     * @param socialid
     * @param organization
     * @param designation
     * @param categoryid
     * @param address
     * @param email
     * @param dob
     * @param gender
     * @param mobile1
     * @param working
     * @param skype
     * @param marital
     */
    public ProfileDetail(String email, String socialid, String phone, String password, String categoryid, String firstname, String middlename, String lastname, String picture, String picturename, String gender, String dob, String marital, String organization, String designation, String working, String address, String countryid, String stateid, String cityid, String zip, String skype, String secondaryEmail, String mobile1, String mobile2, String about) {
        this.email = email;
        this.socialid = socialid;
        this.phone = phone;
        this.password = password;
        this.categoryid = categoryid;
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.picture = picture;
        this.picturename = picturename;
        this.gender = gender;
        this.dob = dob;
        this.marital = marital;
        this.organization = organization;
        this.designation = designation;
        this.working = working;
        this.address = address;
        this.countryid = countryid;
        this.stateid = stateid;
        this.cityid = cityid;
        this.zip = zip;
        this.skype = skype;
        this.secondaryEmail = secondaryEmail;
        this.mobile1 = mobile1;
        this.mobile2 = mobile2;
        this.about = about;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSocialid() {
        return socialid;
    }

    public void setSocialid(String socialid) {
        this.socialid = socialid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicturename() {
        return picturename;
    }

    public void setPicturename(String picturename) {
        this.picturename = picturename;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMarital() {
        return marital;
    }

    public void setMarital(String marital) {
        this.marital = marital;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getWorking() {
        return working;
    }

    public void setWorking(String working) {
        this.working = working;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountryid() {
        return countryid;
    }

    public void setCountryid(String countryid) {
        this.countryid = countryid;
    }

    public String getStateid() {
        return stateid;
    }

    public void setStateid(String stateid) {
        this.stateid = stateid;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getSecondaryEmail() {
        return secondaryEmail;
    }

    public void setSecondaryEmail(String secondaryEmail) {
        this.secondaryEmail = secondaryEmail;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
