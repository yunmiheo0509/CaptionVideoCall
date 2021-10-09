package com.cationvideocall.example.captionvideocall;

import com.google.gson.annotations.SerializedName;

public class CallBookListModel {
    @SerializedName("counter_id")
    String counter_id;
    @SerializedName("name")
    String name;
    @SerializedName("bookmark")
    String bookmark;

    public CallBookListModel(String counter_id, String name, String bookmark) {
        this.counter_id = counter_id;
        this.name = name;
        this.bookmark = bookmark;
    }

    public String getCounter_id() {
        return counter_id;
    }

    public void setCounter_id(String counter_id) {
        this.counter_id = counter_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBookmark() {
        return bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }
}
