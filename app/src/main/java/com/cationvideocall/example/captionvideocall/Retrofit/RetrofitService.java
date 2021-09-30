package com.cationvideocall.example.captionvideocall.Retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitService {
    @FormUrlEncoded
    @POST(Common.LoginURL)
    Call<JSONObject> getLoginCheck(@Field("user_id") String user_id,
                                   @Field("password") String password);


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
    @POST(Common.AcceptURL)
    Call<JsonObject> getAccept(@Field("user_id") String user_id);

}
