package com.thoughtworks.todo_list.repository.user;

import com.thoughtworks.todo_list.repository.user.entity.User;
import com.thoughtworks.todo_list.repository.utils.GsonUtil;
import com.thoughtworks.todo_list.ui.login.UserRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserRepositoryImpl implements UserRepository {
    private UserDataSource dataSource;
    private final static String URL = "https://twc-android-bootcamp.github.io/fake-data/data/user.json";

    public UserRepositoryImpl(UserDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Maybe<User> findByName(String name) {
        return dataSource.findByName(name);
    }

    @Override
    public Completable save(User user) {
        return dataSource.save(user);
    }

    @Override
    public Maybe<User> findUserByName(String username) {
        return Maybe.create(emitter -> {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request
                    .Builder()
                    .url(URL)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            String usersData = response.body().string();
            User user = GsonUtil.stringFromJson(usersData, User.class);
            if (username.equals(user.getName())) {
                emitter.onSuccess(user);
            }
            emitter.onComplete();
        });
    }
}