package com.yar.touchbanktask.entity.json.media;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yar.touchbanktask.entity.realm.RealmString;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class MediaItem extends RealmObject implements Comparable<MediaItem> {

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("comments")
    @Expose
    private Comments comments;

    @SerializedName("caption")
    @Expose
    private Caption caption;

    @SerializedName("likes")
    @Expose
    private Likes likes;

    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("created_time")
    @Expose
    private long createdTime;

    @SerializedName("images")
    @Expose
    private Images images;

    @SerializedName("videos")
    @Expose
    private Videos videos;

//    @SerializedName("users_in_photo")
//    @Expose
//    private RealmList<Object> usersInPhoto;

    @SerializedName("filter")
    @Expose
    private String filter;

    @SerializedName("tags")
    @Expose
    private RealmList<RealmString> tags;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("location")
    @Expose
    private Location location;


    public String getType() {
        return type;
    }

    public Comments getComments() {
        return comments;
    }

    public Caption getCaption() {
        return caption;
    }

    public Likes getLikes() {
        return likes;
    }

    public String getLink() {
        return link;
    }

    public User getUser() {
        return user;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public Images getImages() {
        return images;
    }

    public Videos getVideos() {
        return videos;
    }

//    public List<Object> getUsersInPhoto() {
//        return usersInPhoto;
//    }

    public String getFilter() {
        return filter;
    }

    public List<RealmString> getTags() {
        return tags;
    }

    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaItem mediaItem = (MediaItem) o;

        if (createdTime != mediaItem.createdTime) return false;
        return id != null ? id.equals(mediaItem.id) : mediaItem.id == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (createdTime ^ (createdTime >>> 32));
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(MediaItem o) {
        return (createdTime < o.getCreatedTime()) ? 1 : ((createdTime == o.getCreatedTime()) ? 0 : -1);
    }
}
