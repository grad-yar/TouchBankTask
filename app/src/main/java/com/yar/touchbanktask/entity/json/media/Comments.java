package com.yar.touchbanktask.entity.json.media;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Comments extends RealmObject {

    @SerializedName("count")
    @Expose
    private long count;

    public long getCount() {
        return count;
    }
}
