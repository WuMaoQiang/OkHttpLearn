package com.okhttplearn.retrofitrxjava;

import com.okhttplearn.bean.RetrofitBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface RetrofitRxjavaInterface {
    @GET("wxarticle/chapters/json")
    Observable<RetrofitBean> getInfoRxjava();

}
