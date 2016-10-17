package com.yar.touchbanktask.model;


import com.yar.touchbanktask.entity.json.media.RecentMedia;
import com.yar.touchbanktask.entity.json.user_info.UserInfo;
import com.yar.touchbanktask.rest.InstagramEndpoints;
import com.yar.touchbanktask.util.AsyncIOTransformer;
import com.yar.touchbanktask.util.NetworkUtil;
import com.yar.touchbanktask.util.caching.DataSourceProvider;
import com.yar.touchbanktask.util.caching.SimpleCachingProvider;

import java.util.concurrent.TimeUnit;

import rx.Observable;

public class UserModel implements NetworkUtil.NetworkStatusChangeListener {

    private DataSourceProvider<RecentMedia> mRecentMediaProvider;
    private DataSourceProvider<UserInfo> mUserInfoProvider;

    public UserModel(InstagramEndpoints.UserEndpoints userEndpoints) {

        mRecentMediaProvider = new SimpleCachingProvider<>(
                userEndpoints.getRecentMedia(), RecentMedia.class, 10, TimeUnit.SECONDS);

        mUserInfoProvider = new SimpleCachingProvider<>(
                userEndpoints.getUserInfo(), UserInfo.class, 20, TimeUnit.SECONDS);
    }


    public Observable<UserInfo> getUserInfo() {
        return toAsyncIO(mUserInfoProvider.sink());
    }

    public Observable<RecentMedia> getRecentMedia() {
        return toAsyncIO(mRecentMediaProvider.sink());
    }

    public Observable<RecentMedia> getFreshRecentMedia() {
        return toAsyncIO(mRecentMediaProvider.network());
    }

    private <T> Observable<T> toAsyncIO(Observable<T> observable) {
        return observable.compose(new AsyncIOTransformer<>());
    }

    @Override
    public void onNetworkStatusChange(boolean available) {
        mRecentMediaProvider.onNetworkStatusChange(available);
        mUserInfoProvider.onNetworkStatusChange(available);
    }
}
