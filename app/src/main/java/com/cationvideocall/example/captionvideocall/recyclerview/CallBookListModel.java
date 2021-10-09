package com.cationvideocall.example.captionvideocall.recyclerview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CallBookListModel {

    @SerializedName("counter_id")
    @Expose
    private String counterId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("bookmark")
    @Expose
    private Boolean bookmark;

    public String getCounterId() {
        return counterId;
    }

    public void setCounterId(String counterId) {
        this.counterId = counterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getBookmark() {
        return bookmark;
    }

    public void setBookmark(Boolean bookmark) {
        this.bookmark = bookmark;
    }

}
//public class CallBookListModel {
//    @SerializedName("counter_id")
//    String counter_id;
//    @SerializedName("name")
//    String name;
//    @SerializedName("bookmark")
//    boolean bookmark;
//
//    public CallBookListModel(String counter_id, String name, boolean bookmark) {
//        this.counter_id = counter_id;
//        this.name = name;
//        this.bookmark = bookmark;
//    }
//
//    public String getCounter_id() {
//        return counter_id;
//    }
//
//    public void setCounter_id(String counter_id) {
//        this.counter_id = counter_id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public boolean getBookmark() {
//        return bookmark;
//    }
//}
