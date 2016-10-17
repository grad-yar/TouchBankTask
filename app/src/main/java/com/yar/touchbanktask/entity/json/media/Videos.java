package com.yar.touchbanktask.entity.json.media;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Videos extends RealmObject {

    @SerializedName("low_resolution")
    @Expose
    private MediaMeta lowResolution;

    @SerializedName("standard_resolution")
    @Expose
    private MediaMeta standardResolution;

    public MediaMeta getLowResolution() {
        return lowResolution;
    }

    public MediaMeta getStandardResolution() {
        return standardResolution;
    }
}

