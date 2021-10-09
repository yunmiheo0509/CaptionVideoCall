package com.cationvideocall.example.captionvideocall.Retrofit;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitService {
    @FormUrlEncoded
    @POST(Common.LoginURL)
    Call<JsonObject> getLoginCheck(@Field("user_id") String user_id,
                                   @Field("password") String password,
                                   @Field("token") String token);


    @FormUrlEncoded
    @POST(Common.RegisterURL)
    Call<JsonObject> getRegister(@Field("user_id") String user_id,
                                   @Field("password") String password,
                                   @Field("token") String token);
    @FormUrlEncoded
    @POST(Common.proposeURL)
    Call<JsonObject> getPropose(@Field("user_id") String user_id,
                                 @Field("counter_id") String counter_id);

    @FormUrlEncoded
    @POST(Common.cancelCallURL)
    Call<JsonObject> cancelCall(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(Common.ResponseURL)
    Call<JsonObject> getResponse(@Field("user_id") String user_id,
                               @Field("response") boolean response);
    @FormUrlEncoded
    @POST(Common.AddCallBookURL)
    Call<JsonObject> addCallBook(@Field("user_id") String user_id,
                                 @Field("counter_id") String counter_id,
                                 @Field("name") String name,
                                 @Field("bookmark") boolean bookmark);

}
