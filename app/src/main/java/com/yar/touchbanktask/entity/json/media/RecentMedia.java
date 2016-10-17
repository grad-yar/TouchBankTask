package com.yar.touchbanktask.entity.json.media;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yar.touchbanktask.util.caching.TimeStampedData;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class RecentMedia extends RealmObject implements TimeStampedData {

    @SerializedName("data")
    @Expose
    private RealmList<MediaItem> mediaItems;
    private long timeFetched;

    public List<MediaItem> getMediaItems() {
        return mediaItems;
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
