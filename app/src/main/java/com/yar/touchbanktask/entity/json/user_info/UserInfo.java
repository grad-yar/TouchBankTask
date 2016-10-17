package com.yar.touchbanktask.entity.json.user_info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yar.touchbanktask.util.caching.TimeStampedData;

import io.realm.RealmObject;


public class UserInfo extends RealmObject implements TimeStampedData {

    @SerializedName("data")
    @Expose
    private UserInfoData userInfoData; //for json conversion
    private long timeFetched;

    public Counts getCounts() {
        return userInfoData.getCounts();
    }

    public String getWebsite() {
        return userInfoData.getWebsite();
    }

    public String getBio() {
        return userInfoData.getBio();
    }

    public String getProfilePicture() {
        return userInfoData.getProfilePictureURL();
    }

    public String getFullName() {
        return userInfoData.getFullName();
    }

    public String getUsername() {
        return userInfoData.getUsername();
    }

    public String getId() {
        return userInfoData.getId();
    }

    @Override
    public long getTimeFetched() {
        return timeFetched;
    }

    @Override
    public void updateTimeFetched() {
        timeFetched = System.currentTimeMillis();
    }
}
