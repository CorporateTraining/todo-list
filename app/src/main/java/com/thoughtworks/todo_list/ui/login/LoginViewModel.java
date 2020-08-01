package com.thoughtworks.todo_list.ui.login;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.thoughtworks.todo_list.R;
import com.thoughtworks.todo_list.repository.utils.Encryptor;
import com.thoughtworks.todo_list.ui.login.model.UserModel;

import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.thoughtworks.todo_list.ui.login.LoginActivity.SHARED_ID;
import static com.thoughtworks.todo_list.ui.login.LoginActivity.SHARED_NAME;
import static com.thoughtworks.todo_list.ui.login.LoginActivity.SHARED_USER;

public class LoginViewModel extends AndroidViewModel {
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private UserRepository userRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final static String TAG = "LoginViewModel";

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    void observeLoginResult(LifecycleOwner lifecycleOwner, Observer<LoginResult> observer) {
        loginResult.observe(lifecycleOwner, observer);
    }

    public void login(String username, String password) {
        userRepository.findByName(username)
                .subscribeOn(Schedulers.io())
                .switchIfEmpty(userRepository.getUserByNetwork()
                        .filter(user -> username.equals(user.getName()))
                        .doOnSuccess(this::saveUserData))
                .subscribe(new MaybeObserver<UserModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(UserModel user) {
                        Log.d(TAG, "onNext: ");
                        if (user.getPassword().equals(Encryptor.md5(password))) {
                            loginResult.postValue(new LoginResult(new LoggedInUserView(user.getName())));
                            SharedPreferences preferences = getApplication().getSharedPreferences(SHARED_USER, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt(SHARED_ID, user.getId());
                            editor.putString(SHARED_NAME, user.getName());
                            editor.apply();
                            return;
                        }
                        loginResult.postValue(new LoginResult(R.string.login_failed_password));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ", e);
                    }

                    @Override
                    public void onComplete() {
                        loginResult.postValue(new LoginResult(R.string.login_failed_username));
                        Log.d(TAG, "onComplete: ");
                    }
                });
    }

    public void saveUserData(UserModel user) {
        Disposable subscribe = userRepository.save(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        compositeDisposable.add(subscribe);
    }

    @Override
    protected void onCleared() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.onCleared();
    }
}