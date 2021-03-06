package com.thoughtworks.todo_list.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.thoughtworks.todo_list.MainApplication;
import com.thoughtworks.todo_list.R;
import com.thoughtworks.todo_list.ui.login.model.UserModel;
import com.thoughtworks.todo_list.ui.task.TaskActivity;
import com.thoughtworks.todo_list.ui.utils.Validator;

import org.jetbrains.annotations.NotNull;

import static com.thoughtworks.todo_list.ui.utils.Validator.PASSWORD_REGULAR;
import static com.thoughtworks.todo_list.ui.utils.Validator.USERNAME_REGULAR;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private ProgressBar loadingProgressBar;
    public final static String SHARED_USER = "user";
    public final static String SHARED_ID = "id";
    public final static String SHARED_NAME = "name";
    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        autoLogin();
        loginViewModel = obtainViewModel();
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);
        observeLoginResult();
        TextWatcher afterTextChangedListener = getAfterTextChangedListener(usernameEditText, passwordEditText, loginButton);
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        listenerPasswordKeyboardActionDone();
        listenerLogin();
    }

    private void autoLogin() {
        preferences = getApplication().getSharedPreferences(SHARED_USER, Context.MODE_PRIVATE);
        int userId = preferences.getInt(SHARED_ID, 0);
        if (userId != 0) {
            Intent intent = new Intent(this, TaskActivity.class);
            startActivity(intent);
        }
    }

    private void listenerLogin() {
        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            login();
        });
    }

    private void login() {
        loginViewModel.login(usernameEditText.getText().toString(),
                passwordEditText.getText().toString());
    }

    private void listenerPasswordKeyboardActionDone() {
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login();
            }
            return false;
        });
    }

    private void observeLoginResult() {
        loginViewModel.observeLoginResult(this, loginResult -> {
            if (loginResult == null) {
                return;
            }
            loadingProgressBar.setVisibility(View.GONE);
            if (loginResult.getError() != null) {
                showLoginFailed(loginResult.getError());
            }
            if (loginResult.getSuccess() != null) {
                showLoginFailed(R.string.login_welcome);
                Intent intent = new Intent(this, TaskActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @NotNull
    private TextWatcher getAfterTextChangedListener(EditText usernameEditText, EditText passwordEditText, Button loginButton) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (isTextValueInvalid(USERNAME_REGULAR, username)) {
                    loginButton.setEnabled(false);
                    usernameEditText.setError(getString(R.string.invalid_username));
                } else if (isTextValueInvalid(PASSWORD_REGULAR, password)) {
                    loginButton.setEnabled(false);
                    passwordEditText.setError(getString(R.string.invalid_password));
                } else {
                    loginButton.setEnabled(true);
                }
            }
        };
    }

    private void showLoginFailed(Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }


    private LoginViewModel obtainViewModel() {
        UserRepository userRepository = (((MainApplication) getApplicationContext())).userRepository();
        LoginViewModel loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.setUserRepository(userRepository);
        return loginViewModel;
    }

    private boolean isTextValueInvalid(String regular, String text) {
        if (text == null) {
            return true;
        }
        return !Validator.isValid(regular, text.trim());
    }
}