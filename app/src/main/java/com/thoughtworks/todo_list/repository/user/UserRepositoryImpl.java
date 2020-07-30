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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserRepositoryImpl implements UserRepository {
    private UserDataSource dataSource;

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
}