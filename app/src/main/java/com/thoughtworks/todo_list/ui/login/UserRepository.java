package com.thoughtworks.todo_list.ui.login;

import com.thoughtworks.todo_list.repository.user.entity.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

public interface UserRepository {
    Maybe<User> findByName(String name);

    Completable save(User user);

    Maybe<User> findUserByNetwork();
}
