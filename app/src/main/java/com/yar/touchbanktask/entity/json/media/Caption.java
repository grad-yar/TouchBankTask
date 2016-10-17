package com.yar.touchbanktask.entity.json.media;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Caption extends RealmObject {

    @SerializedName("created_time")
    @Expose
    private String createdTime;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("from")
    @Expose
    private Author from;

    @SerializedName("id")
    @Expose
    private String id;

    public String getCreatedTime() {
        return createdTime;
    }

    public String getText() {
        return text;
    }

    public Author getFrom() {
        return from;
    }

    public String getId() {
        return id;
    }


}
