package com.thoughtworks.todo_list.repository.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GsonUtil {
    public static Gson gson = new Gson();
    public static <T> T stringFromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
