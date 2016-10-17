package com.yar.touchbanktask.entity.json.media;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Location extends RealmObject {

    @SerializedName("latitude")
    @Expose
    private float latitude;

    @SerializedName("longitude")
    @Expose
    private float longitude;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("street_address")
    @Expose
    private String streetAddress;

    @SerializedName("name")
    @Expose
    private String name;

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getId() {
        return id;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getName() {
        return name;
    }
}
