package com.example.lenovo.cart01.utils;

import android.os.Handler;

import com.example.lenovo.cart01.inter.ICallBack;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lenovo on 2018/10/22.
 */

public class HttpUtils {

    private static volatile HttpUtils instance;

    private OkHttpClient client;

    private Handler handler = new Handler();

    private HttpUtils(){
        client = new OkHttpClient();
    }

    public static HttpUtils getInstance(){

        if (instance == null){
            synchronized (HttpUtils.class){
                if (null == instance){
                    instance = new HttpUtils();
                }
            }
        }
        return instance;
    }


    public void get(String url, final ICallBack callBack , final Type type){

        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onfailed(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();

                Gson gson = new Gson();
                final Object o = gson.fromJson(result, type);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onsuccess(o);
                    }
                });

            }
        });

    }


}
