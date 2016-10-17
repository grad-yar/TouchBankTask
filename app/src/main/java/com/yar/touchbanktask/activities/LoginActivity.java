package com.yar.touchbanktask.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.orhanobut.hawk.Hawk;
import com.yar.touchbanktask.InstagramApp;
import com.yar.touchbanktask.R;
import com.yar.touchbanktask.fragments.LoginDialogFragment;
import com.yar.touchbanktask.util.NetworkUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginDialogFragment.LoginListener {

    public static final String TOKEN_KEY = "token";

    @BindView(R.id.btn_sign_in)
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String token = Hawk.get(TOKEN_KEY);

        if (token == null || token.isEmpty()) {
            setContentView(R.layout.activity_login);
            ButterKnife.bind(this);
        } else {
            onLoginComplete(token);
        }
    }

    @OnClick(R.id.btn_sign_in)
    public void onSignInClicked() {
        if (NetworkUtil.isNetworkConnection(this)) {
            new LoginDialogFragment().show(getFragmentManager(), LoginDialogFragment.class.getName());
        } else {
            Snackbar.make(btnSignIn, R.string.message_no_connection, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoginComplete(String token) {
        Hawk.put(LoginActivity.TOKEN_KEY, token);
        InstagramApp.instance().initModels(token);

        Intent intent = new Intent(this, PostsActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    public void onLoginError() {
        Snackbar.make(btnSignIn, R.string.message_login_error, Snackbar.LENGTH_SHORT).show();
    }
}
