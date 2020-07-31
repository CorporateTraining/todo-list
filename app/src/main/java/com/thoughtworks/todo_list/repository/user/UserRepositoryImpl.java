package com.thoughtworks.todo_list.repository.user;

import com.thoughtworks.todo_list.repository.user.entity.User;
import com.thoughtworks.todo_list.repository.user.model.UserModel;
import com.thoughtworks.todo_list.repository.utils.NetworkGetData;
import com.thoughtworks.todo_list.ui.login.UserRepository;

import io.reactivex.Completable;
import io.reactivex.Maybe;

import static com.thoughtworks.todo_list.repository.utils.NetworkGetData.URL;

public class UserRepositoryImpl implements UserRepository {
    private UserDataSource dataSource;

    public UserRepositoryImpl(UserDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Maybe<UserModel> findByName(String name) {
        return dataSource.findByName(name).map(user -> new UserModel().build(user));
    }

    @Override
    public Completable save(UserModel userModel) {
        User user = new User().build(userModel);
        return dataSource.save(user);
    }

    @Override
    public Maybe<UserModel> getUserByNetwork() {
        return Maybe.create(emitter -> {
            UserModel userModel = NetworkGetData.getDataFromUrl(URL, UserModel.class);
            emitter.onSuccess(userModel);
            emitter.onComplete();
        });
    }
}