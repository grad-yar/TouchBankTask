package com.yar.touchbanktask.entity.json.user_info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Counts extends RealmObject {

    @SerializedName("media")
    @Expose
    private int media;
    @SerializedName("follows")
    @Expose
    private int follows;
    @SerializedName("followed_by")
    @Expose
    private int followedBy;

    public int getMedia() {
        return media;
    }


    public int getFollows() {
        return follows;
    }


    public int getFollowedBy() {
        return followedBy;
    }

}
