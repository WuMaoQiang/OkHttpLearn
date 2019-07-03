package com.okhttplearn.retrofit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.okhttplearn.R;
import com.okhttplearn.bean.RetrofitBean;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitActivity extends AppCompatActivity {

    private TextView textView;
    private final String BASEURL = "https://wanandroid.com/";
    private final String BASEURL2 = "http://192.168.1.100:8080/";


    private TextView textView2;
    OkHttpClient.Builder okHttpClientBuilder;
    private TextView textView3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retrofit_main);
        initView();
        Request();
        okHttpClientBuilder = new OkHttpClient.Builder();
//启用Log日志
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.addInterceptor(loggingInterceptor);
    }


    private void initView() {
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request2();
            }
        });
        textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request3();
            }
        });
    }


    private void Request() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASEURL).addConverterFactory(GsonConverterFactory.create()).build();

        RetroiftInterface retroiftInterface = retrofit.create(RetroiftInterface.class);

        Call<RetrofitBean> callGetinfo = retroiftInterface.getinfo();

        callGetinfo.enqueue(new Callback<RetrofitBean>() {
            @Override
            public void onResponse(Call<RetrofitBean> call, Response<RetrofitBean> response) {
                textView.setText(response.body().getData().get(0).getName());
            }

            @Override
            public void onFailure(Call<RetrofitBean> call, Throwable t) {

            }
        });

    }

    private void Request2() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASEURL).client(okHttpClientBuilder.build()).addConverterFactory(GsonConverterFactory.create()).build();

        RetroiftInterface retroiftInterface = retrofit.create(RetroiftInterface.class);

        Call<RetrofitBean> callGetinfo = retroiftInterface.login("wumaoqiang@vip.qq.com", "cc719014");

        callGetinfo.enqueue(new Callback<RetrofitBean>() {
            @Override
            public void onResponse(Call<RetrofitBean> call, Response<RetrofitBean> response) {
                textView.setText(response.body().getErrorMsg());
            }

            @Override
            public void onFailure(Call<RetrofitBean> call, Throwable t) {

            }
        });

    }

    private void Request3() {
        //使用趣动的上传图片的功能
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASEURL2).client(okHttpClientBuilder.build()).addConverterFactory(GsonConverterFactory.create()).build();

        RetroiftInterface retroiftInterface = retrofit.create(RetroiftInterface.class);
        MediaType textType = MediaType.parse("text/plain");
        RequestBody storId = RequestBody.create(textType, "355090187434328064");
        RequestBody file = RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), new File("/storage/emulated/0/aaa.png"));

        // @Part
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", "aaa.png", file);


        Call<RetrofitBean> callGetinfo = retroiftInterface.upLoad(storId, filePart);

        callGetinfo.enqueue(new Callback<RetrofitBean>() {
            @Override
            public void onResponse(Call<RetrofitBean> call, Response<RetrofitBean> response) {
                textView.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<RetrofitBean> call, Throwable t) {

            }
        });


    }
}
