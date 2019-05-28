package com.example.demo;

import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkUtils {
    OkHttpClient okHttpClient;
    private OkUtils(){
        Log.d("aaa","创建一次");
        okHttpClient=new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5,TimeUnit.SECONDS)
                .build();
    }
    private static OkUtils okUtils=new OkUtils();

    public static OkUtils getOkUtils() {
        return okUtils;
    }
    public void doGet(String url,final MyCallBack myCallBack){
        final Request request= new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myCallBack.error(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                myCallBack.success(response.body().string());
            }
        });
    }
    public void doPoast(String url,final MyCallBack myCallBack,String[] keyname,String[] value){
        FormBody.Builder builder = new FormBody.Builder();
        for (int i=0;i<keyname.length;i++){
            builder.add(keyname[i],value[i]);
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .post(formBody)
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myCallBack.error(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                myCallBack.success(response.body().string());
            }
        });
    }
    public void downLoad(String url,final MyCallBack myCallBack){
        final Request request = new Request.Builder().get()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myCallBack.error(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = new FileOutputStream("/sdcard/Pictures/aaa.text");
                byte[] bytes = new byte[1024];
                int len=0;
                while ((len=inputStream.read(bytes))!=-1){
                    fileOutputStream.write(bytes,0,len);
                }
                 fileOutputStream.flush();
                 fileOutputStream.close();

            }
        });
    }
    interface  MyCallBack{
        public void success(String str);
        public void error(String mesage);
    }
}
