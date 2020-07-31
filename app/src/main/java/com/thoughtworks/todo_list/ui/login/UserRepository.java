package com.thoughtworks.todo_list.ui.login;

import com.thoughtworks.todo_list.repository.user.model.UserModel;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public interface UserRepository {
    Maybe<UserModel> findByName(String name);

    Completable save(UserModel user);

    Maybe<UserModel> getUserByNetwork();
}
