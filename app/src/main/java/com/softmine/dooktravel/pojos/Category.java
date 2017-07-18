package com.softmine.dooktravel.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by GAURAV on 7/16/2017.
 */

public class Category implements Serializable{
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("catego ry_name")
    @Expose
    private String categoRyName;

    /**
     * No args constructor for use in serialization
     *
     */
    public Category() {
    }

    /**
     *
     * @param categoryName
     * @param categoryId
     * @param categoRyName
     */
    public Category(String categoryId, String categoryName, String categoRyName) {
        super();
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoRyName = categoRyName;
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

    public String getCategoRyName() {
        return categoRyName;
    }

    public void setCategoRyName(String categoRyName) {
        this.categoRyName = categoRyName;
    }
}
