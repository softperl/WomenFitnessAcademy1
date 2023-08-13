package com.women.fitnessacademy.views.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;

import com.google.gson.Gson;
import com.women.fitnessacademy.R;
import com.women.fitnessacademy.core.config.Config;
import com.women.fitnessacademy.core.data.Constants;
import com.women.fitnessacademy.core.data.DataManager;
import com.women.fitnessacademy.core.data.model.User;
import com.women.fitnessacademy.core.ui.contracts.LoginContract;
import com.women.fitnessacademy.core.ui.presenters.LoginPresenter;

public class LoginActivity extends BaseActivity implements LoginContract.LoginView {

    TextView forgotPassword, skip;
    Button login, createAccount;
    AppCompatEditText username, password;
    ProgressBar progressBar;
    private LoginPresenter mPresenter;
    CheckBox checkPP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkPP= findViewById(R.id.checkPP);
        TextView textView= findViewById(R.id.movementText);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        mPresenter = new LoginPresenter(DataManager.getInstance(this));
        initView();
        mPresenter.attachView(this);
        setupClicks();
    }

    private void setupClicks() {
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, WebActivity.class);
                intent.putExtra(WebActivity.URL, Config.REGISTER_URL);
                intent.putExtra(WebActivity.CSS, "custom.css");
                intent.putExtra(WebActivity.TITLE, "Register");
                intent.putExtra(WebActivity.MESSAGE, "Successfully Registered");
                startActivity(intent);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPP.isChecked()) {
                    if (!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                        mPresenter.onRequestLoginWithWordpress(username.getText().toString().trim(), password.getText().toString().trim());
                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    } else {
                        Toast.makeText(getApplicationContext(), "Username or Password should not be empty", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "Please check Privacy Policy ",Toast.LENGTH_SHORT).show();
                }
            }
        });


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, WebActivity.class);
                intent.putExtra(WebActivity.URL, Config.LOST_PASSWORD_URL);
                intent.putExtra(WebActivity.CSS, "custom.css");
                intent.putExtra(WebActivity.TITLE, "Forgot Password");
                intent.putExtra(WebActivity.MESSAGE, "Password Sent to your email. Check Email");
                startActivity(intent);
            }
        });


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initView() {
        login = findViewById(R.id.login);
        createAccount = findViewById(R.id.create_account);
        username = findViewById(R.id.username);
        skip = findViewById(R.id.skip);
        forgotPassword = findViewById(R.id.forgot_password);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progress_bar);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        username.setEnabled(false);
        password.setEnabled(false);
        login.setEnabled(false);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
        login.setEnabled(true);
    }

    @Override
    public void showError(String errorMessage) {
        username.setEnabled(true);
        password.setEnabled(true);

        login.setEnabled(true);
        if (errorMessage.equals("Default 403 Forbidden")) {
            Toast.makeText(getApplicationContext(), R.string.username_password_not_correct, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWordpressLoginResponse(boolean success, User user, String token) {
        if (success) {
            applicationMain.user = user;
            Gson gson = new Gson();
            String json = gson.toJson(user);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            sharedPreferences.edit().putString(Constants.KEY_FOR_TOKEN, token).apply();
            sharedPreferences.edit().putString(Constants.KEY_FOR_USER, json).apply();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            username.setEnabled(true);
            password.setEnabled(true);
            login.setEnabled(true);
            Toast.makeText(getApplicationContext(), R.string.username_password_not_correct, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }
}
