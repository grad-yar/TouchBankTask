package com.yar.touchbanktask;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.webkit.WebView;

import com.orhanobut.hawk.Hawk;
import com.yar.touchbanktask.activities.LoginActivity;
import com.yar.touchbanktask.model.UserModel;
import com.yar.touchbanktask.rest.InstagramEndpoints;
import com.yar.touchbanktask.util.CookiesUtil;
import com.yar.touchbanktask.util.NetworkUtil;

import io.realm.Realm;


public class InstagramApp extends Application {

    private static InstagramApp instance;

    public static InstagramApp instance() {
        return instance;
    }

    private NetworkUtil.NetworkStatusReceiver mNetworkStatusReceiver;

    private UserModel mUserModel;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Realm.init(this);
        Hawk.init(this).build();

        mNetworkStatusReceiver = NetworkUtil.registerNetworkStatusReceiver(this);
    }

    public void initModels(String token) {
        InstagramEndpoints.initEndpoints(token);
        mUserModel = new UserModel(InstagramEndpoints.getUserEndpoints());
        mNetworkStatusReceiver.removeAllListeners();
        mNetworkStatusReceiver.addListener(available ->
                Log.d("NETWORK", "Network available: " + available)
        );
        mNetworkStatusReceiver.addListener(mUserModel);
    }

    public void logOut() {
        //clear webivew and cookoies
        WebView webView = new WebView(this);
        webView.clearCache(true);
        webView.clearHistory();
        webView.clearFormData();
        CookiesUtil.clearCookies(this);

        //clear disk cache
        Realm defaultInstance = Realm.getDefaultInstance();
        defaultInstance.close();
        Realm.deleteRealm(defaultInstance.getConfiguration());

        //clear token
        Hawk.delete(LoginActivity.TOKEN_KEY);

        //launch login activity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public UserModel getUserModel() {
        return mUserModel;
    }
}
