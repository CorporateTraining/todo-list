package com.thoughtworks.todo_list.repository.utils;

import com.thoughtworks.todo_list.repository.user.entity.User;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkGetData {
    public final static String URL = "https://twc-android-bootcamp.github.io/fake-data/data/user.json";

    public static <T> T getDataFromUrl(String url, Class<T> tClass){
        T t = null;
        try {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request
                .Builder()
                .url(url)
                .build();
        Response response = okHttpClient.newCall(request).execute();
            String usersData = response.body().string();
            t = GsonUtil.stringFromJson(usersData, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }
}
