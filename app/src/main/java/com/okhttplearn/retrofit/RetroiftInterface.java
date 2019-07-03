package com.okhttplearn.retrofit;

import com.okhttplearn.bean.RetrofitBean;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetroiftInterface {


    @GET("wxarticle/chapters/json")
    Call<RetrofitBean> getinfo();


    @FormUrlEncoded
    @POST("user/login")
    Call<RetrofitBean> login(@Field("username") String name, @Field("password") String password);

    @POST("consultant/report/uploadImages?token=bbdbeb1e3ee342aa96be0404decc60d4&studentId=577094351495102464")
    @Multipart
    Call<RetrofitBean>  upLoad(@Part("storeId") RequestBody storeId, @Part MultipartBody.Part file);
}
