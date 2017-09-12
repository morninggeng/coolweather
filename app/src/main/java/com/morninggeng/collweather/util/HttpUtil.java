package com.morninggeng.collweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 请求网络
 * Created by morninggeng on 2017/9/9.
 */
public class HttpUtil {

    /**
     * 请求网络
     * @param address 地址
     * @param callback 回调
     */
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
