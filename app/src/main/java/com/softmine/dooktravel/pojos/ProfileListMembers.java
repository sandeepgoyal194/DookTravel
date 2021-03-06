package com.softmine.dooktravel.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by GAURAV on 7/20/2017.
 */

public class ProfileListMembers implements Serializable {
    @SerializedName("member_id")
    @Expose
    private String memberId;
    @SerializedName("email_id")
    @Expose
    private String emailId;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("middle_name")
    @Expose
    private String middleName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("date_of_birth")
    @Expose
    private String dateOfBirth;
    @SerializedName("marital_status")
    @Expose
    private String maritalStatus;
    @SerializedName("organization")
    @Expose
    private String organization;
    @SerializedName("working_since")
    @Expose
    private String workingSince;
    @SerializedName("addr ess")
    @Expose
    private String addrEss;
    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("state_name")
    @Expose
    private String stateName;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("zip_code")
    @Expose
    private String zipCode;
    @SerializedName("skype")
    @Expose
    private String skype;
    @SerializedName("about")
    @Expose
    private String about;

    /**
     * No args constructor for use in serialization
     *
     */
    public ProfileListMembers() {
    }

    /**
     *
     * @param countryId
     * @param middleName
     * @param dateOfBirth
     * @param lastName
     * @param countryName
     * @param phone
     * @param cityName
     * @param categoryId
     * @param about
     * @param addrEss
     * @param maritalStatus
     * @param city
     * @param workingSince
     * @param categoryName
     * @param organization
     * @param emailId
     * @param stateId
     * @param zipCode
     * @param stateName
     * @param profilePic
     * @param gender
     * @param memberId
     * @param firstName
     * @param skype
     */
    public ProfileListMembers(String memberId, String emailId, String phone, String categoryId, String categoryName, String firstName, String middleName, String lastName, String profilePic, String gender, String dateOfBirth, String maritalStatus, String organization, String workingSince, String addrEss, String countryId, String countryName, String stateId, String stateName, String city, String cityName, String zipCode, String skype, String about) {
        super();
        this.memberId = memberId;
        this.emailId = emailId;
        this.phone = phone;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.profilePic = profilePic;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.maritalStatus = maritalStatus;
        this.organization = organization;
        this.workingSince = workingSince;
        this.addrEss = addrEss;
        this.countryId = countryId;
        this.countryName = countryName;
        this.stateId = stateId;
        this.stateName = stateName;
        this.city = city;
        this.cityName = cityName;
        this.zipCode = zipCode;
        this.skype = skype;
        this.about = about;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getWorkingSince() {
        return workingSince;
    }

    public void setWorkingSince(String workingSince) {
        this.workingSince = workingSince;
    }

    public String getAddrEss() {
        return addrEss;
    }

    public void setAddrEss(String addrEss) {
        this.addrEss = addrEss;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

}
