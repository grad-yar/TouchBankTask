package com.yar.touchbanktask.util.caching;

import android.util.Log;
import android.util.LruCache;

import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmObject;
import rx.Observable;

// TODO implement smarter caching for paging in the future
public class SimpleCachingProvider<T extends RealmObject & TimeStampedData> implements DataSourceProvider<T> {

    private static final String TAG = SimpleCachingProvider.class.getSimpleName();

    private Class<T> mTClass;
    private String mParamClassName;

    private Observable<T> mNetworkObservable;
    private LruCache<String, T> mLruCache; //redundant but when i figure out how to implement paging it will be useful

    private volatile boolean isConnected = false;
    private long mDataStaleIntervalMillis;

    public SimpleCachingProvider(Observable<T> networkObservable, Class<T> tClass,
                                 long dataStaleInterval, TimeUnit timeUnit) {
        mNetworkObservable = networkObservable;
        mLruCache = new LruCache<>(10);
        mTClass = tClass;
        mParamClassName = mTClass.getSimpleName();

        mDataStaleIntervalMillis = timeUnit.toMillis(dataStaleInterval);
    }

    @Override
    public Observable<T> memory() {
        return Observable.fromCallable(this::getFromMemory)
                .filter(media -> {
                    boolean isEmpty = media == null;
                    if (isEmpty) {
                        Log.d(TAG, "Not found in memory: " + mParamClassName);
                    }
                    return !isEmpty;
                });
    }

    @Override
    public Observable<T> disk() {
        return Observable.fromCallable(this::getFromDisk)
                .filter(media -> media != null)
                .doOnNext(this::saveInMemory);
    }

    @Override
    public Observable<T> network() {
        return mNetworkObservable
                .doOnNext(data -> {
                    Log.d(TAG, "Fetching from network: " + mParamClassName);

                    data.updateTimeFetched();

                    saveInMemory(data);
                    saveToDisk(data);
                });
    }

    @Override
    public Observable<T> sink() {
        return Observable.concat(memory(), disk(), network())
                .takeFirst(this::isFresh);
    }

    private boolean isFresh(T data) {
        return !isConnected || System.currentTimeMillis() - data.getTimeFetched() < mDataStaleIntervalMillis;
    }

    private void saveToDisk(T data) {
        Log.d(TAG, "Saving to disk: " + mParamClassName);
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(r -> {
                r.delete(mTClass);
                r.insert(data);
            });
        } finally {
            realm.close();
        }

    }

    private T getFromDisk() {
        Log.d(TAG, "Loading from disk: " + mParamClassName);

        Realm realm = Realm.getDefaultInstance();
        T data;
        try {
            data = realm.where(mTClass).findFirst();

            if (data != null) {
                data = realm.copyFromRealm(data);
            }
        } finally {
            realm.close();
        }

        return data;
    }

    private void saveInMemory(T data) {
        synchronized (mLruCache) {
            Log.d(TAG, "Saving in memory: " + mParamClassName);
            mLruCache.put(mParamClassName, data);
        }
    }

    private T getFromMemory() {
        T data;

        synchronized (mLruCache) {
            Log.d(TAG, "Loading from memory: " + mParamClassName);
            data = mLruCache.get(mParamClassName);
        }

        return data;
    }

    @Override
    public void onNetworkStatusChange(boolean available) {
        isConnected = available;
    }
}
