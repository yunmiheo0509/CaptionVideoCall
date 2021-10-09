package com.cationvideocall.example.captionvideocall.recyclerview;

import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.Expose;
import java.util.List;

//public class SearchResultModel {
//    @SerializedName("code")
//    public int code;
//
//    @SerializedName("results")
//    public List<CallBookListModel> results;
//
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public SearchResultModel(int code) {
//        this.code = code;
//    }
//}

//@Generated("jsonschema2pojo")
public class SearchResultModel {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("result")
    @Expose
    private List<CallBookListModel> result = null;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<CallBookListModel> getResult() {
        return result;
    }

    public void setResult(List<CallBookListModel> result) {
        this.result = result;
    }

}