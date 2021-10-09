package com.cationvideocall.example.captionvideocall;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResultModel {
    @SerializedName("code")
    public String code;

    @SerializedName("results")
    public List<CallBookListModel> results;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SearchResultModel(String code) {
        this.code = code;
    }
}
