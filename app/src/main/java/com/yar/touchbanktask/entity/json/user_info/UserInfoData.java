package com.yar.touchbanktask.entity.json.user_info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class UserInfoData extends RealmObject {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("profile_picture")
    @Expose
    private String profilePictureURL;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("counts")
    @Expose
    private Counts counts;

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public String getBio() {
        return bio;
    }

    public String getWebsite() {
        return website;
    }

    public Counts getCounts() {
        return counts;
    }
}
