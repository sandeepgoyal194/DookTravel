package com.softmine.dooktravel.pojos;

/**
 * Created by GAURAV on 7/1/2017.
 */

public class Model {

    private  String name;
    private  String company;
    private  String address;

    public Model(String name, String company, String address) {
        this.name = name;
        this.company = company;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
