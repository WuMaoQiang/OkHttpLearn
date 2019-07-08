package com.okhttplearn;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.okhttplearn.retrofit.RetrofitActivity;
import com.okhttplearn.retrofitrxjava.RetrofitRxjavaActivity;
import com.tencent.mmkv.MMKV;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "xiaoqiang";
    private TextView mAaa;
    private Button button;
    private Button button2;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MMKV.initialize(this);


        MMKV mMkv = MMKV.mmkvWithID("aaaa", MMKV.SINGLE_PROCESS_MODE);
// 存储数据：
        mMkv.encode("Stingid", "aaaa");
        mMkv.encode("bool", true);
        mMkv.encode("int", 123);

        // 取数据：
        String id = mMkv.decodeString("Stingid", null);
        boolean bValue = mMkv.decodeBool("bool");
        int iValue = mMkv.decodeInt("int");

        Log.i(TAG, "onCreate: "+id+".."+bValue+".."+iValue);

        requestPermission();
        initView();
        requestInfo();
    }

    private void requestInfo() {
        String url = "https://wanandroid.com/wxarticle/chapters/json";
        File sdcache = getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        //1、创建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new CacheInterceptor()).cache(new Cache(sdcache.getAbsoluteFile(), cacheSize)).build();
        //2、构造Request对象
        final Request request = new Request.Builder().addHeader("sd", "dsd")
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        //3、通过Request对象和OkHttpClient对象构建 Call对象
        Call call = okHttpClient.newCall(request);
        //4、通过Call#enqueue(Callback)方法来提交异步请求；
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = response.body().string();//response.body().string()这个方法只能调用一次，所以只要打印了log就不能在调用了

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAaa.setText(res);
                    }
                });
            }

        });
    }

    //设置缓存的第一种方式  addNetworkInterceptor(new CacheNetworkInterceptor())
    class CacheNetworkInterceptor implements Interceptor {
        public Response intercept(Chain chain) throws IOException {
            //无缓存,进行缓存
            return chain.proceed(chain.request()).newBuilder()
                    .removeHeader("Pragma")
                    //对请求进行最大60秒的缓存
                    .addHeader("Cache-Control", "max-age=10")
                    .build();
        }
    }

    //设置缓存的第二种方式 .addInterceptor(new CacheInterceptor())
    // 只是加了有无网络的情况下取缓存时间的区别
    class CacheInterceptor implements Interceptor {
        public Response intercept(Chain chain) throws IOException {
            Response resp;
            Request req;
            if (isAvailable(MainActivity.this)) {
                //有网络,检查10秒内的缓存
                req = chain.request()
                        .newBuilder()
                        .cacheControl(new CacheControl
                                .Builder()
                                .maxAge(10, TimeUnit.SECONDS)
                                .build())
                        .build();
            } else {
                //无网络,检查30天内的缓存,即使是过期的缓存
                req = chain.request().newBuilder()
                        .cacheControl(new CacheControl.Builder()
                                .onlyIfCached()
                                .maxStale(10, TimeUnit.SECONDS)
                                .build())
                        .build();
            }
            resp = chain.proceed(req);
            return resp.newBuilder().build();
        }
    }

    /**
     * 判断当前网络是否可用
     *
     * @param context
     * @return true：可用，false:不可用；
     */
    public static boolean isAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            return false;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        // NetworkInfo.isAvailable() 如果网络可用返回true，否则返回false。
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        } else {
            return true;
        }
    }


    private void requestPermission() {
        PermissionUtils.permission(PermissionConstants.STORAGE).callback(new PermissionUtils.FullCallback() {
            @Override
            public void onGranted(List<String> permissionsGranted) {
//                Log.i(TAG, "onGranted");
            }

            @Override
            public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                if (permissionsDeniedForever.size() != 0) {
                    Log.i(TAG, "拒絕不在提醒去seting");
                    return;
                }
                Log.i(TAG, "拒绝了");

            }
        }).request();
    }


    private void initView() {
        mAaa = (TextView) findViewById(R.id.aaa);


        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), MainActivity2.class);
//                MainActivity.this.startActivity(intent);
                startActivity(new Intent(MainActivity.this, RetrofitActivity.class));
            }
        });

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RetrofitRxjavaActivity.class));
            }
        });

    }
}
