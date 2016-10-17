package com.yar.touchbanktask.entity.json.media;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Images extends RealmObject {

    @SerializedName("thumbnail")
    @Expose
    private MediaMeta thumbnail;

    @SerializedName("low_resolution")
    @Expose
    private MediaMeta lowResolution;

    @SerializedName("standard_resolution")
    @Expose
    private MediaMeta standardResolution;

    public MediaMeta getThumbnail() {
        return thumbnail;
    }

    public MediaMeta getLowResolution() {
        return lowResolution;
    }

    public MediaMeta getStandardResolution() {
        return standardResolution;
    }
}
