package com.remotemonster.example.captionvideocall;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitService {
    @FormUrlEncoded
    @POST(Common.LoginURL)
    Call<JsonObject> getLoginCheck(@Field("user_id") String user_id,
                                   @Field("password") String password);
}
