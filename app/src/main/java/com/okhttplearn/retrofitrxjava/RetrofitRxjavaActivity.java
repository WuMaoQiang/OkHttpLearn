package com.okhttplearn.retrofitrxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.okhttplearn.R;
import com.okhttplearn.bean.RetrofitBean;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRxjavaActivity extends AppCompatActivity {

    private TextView textView;
    private final String BASEURL = "https://wanandroid.com/";
    Retrofit retrofit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retrofit_rxjava_main);
        initView();
        request();
    }

    private void request() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASEURL)
                .build();
        RetrofitRxjavaInterface retrofitRxjavaInterface = retrofit.create(RetrofitRxjavaInterface.class);
        Observable observable = retrofitRxjavaInterface.getInfoRxjava();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RetrofitBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RetrofitBean retrofitBean) {
                        textView.setText(retrofitBean.getData().get(0).getName());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

//        RetrofitRxjavaInterface retrofitRxjavaInterface = retrofit.create(RetrofitRxjavaInterface.class);
//
//        Observable<RetrofitBean> infoRxjava = retrofitRxjavaInterface.getInfoRxjava();
//        infoRxjava.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<RetrofitBean>());
//

    }

    private void initView() {
        textView = (TextView) findViewById(R.id.textView);
    }
}
