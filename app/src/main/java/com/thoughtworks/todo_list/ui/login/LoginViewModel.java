package com.thoughtworks.todo_list.ui.login;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.thoughtworks.todo_list.R;
import com.thoughtworks.todo_list.repository.user.entity.User;
import com.thoughtworks.todo_list.repository.utils.Encryptor;

import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private UserRepository userRepository;
    private Disposable disposable;
    private final static String TAG = "LoginViewModel";

    void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    void observeLoginFormState(LifecycleOwner lifecycleOwner, Observer<LoginFormState> observer) {
        loginFormState.observe(lifecycleOwner, observer);
    }

    void observeLoginResult(LifecycleOwner lifecycleOwner, Observer<LoginResult> observer) {
        loginResult.observe(lifecycleOwner, observer);
    }

    @SuppressLint("CheckResult")
    public void login(String username, String password) {
        userRepository.findUserByName(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(User user) {
                        Log.d(TAG, "onNext: ");
                        if (user == null) {
                            loginResult.postValue(new LoginResult(R.string.login_failed_username));
                            return;
                        }

                        if (user.getPassword().equals(Encryptor.md5(password))) {
                            loginResult.postValue(new LoginResult(new LoggedInUserView(user.getName())));
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

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.postValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.postValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.postValue(new LoginFormState(true));
        }
    }

    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        String trimUsername = username.trim();
        return trimUsername.length() >= 3 && trimUsername.length() <= 12;
    }

    private boolean isPasswordValid(String password) {
        if (password == null) {
            return false;
        }
        String trimPassword = password.trim();
        return trimPassword.length() >= 6 && trimPassword.length() <= 18;
    }

    @Override
    protected void onCleared() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onCleared();
    }
}