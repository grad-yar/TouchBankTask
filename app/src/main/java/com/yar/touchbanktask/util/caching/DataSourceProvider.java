package com.yar.touchbanktask.util.caching;


import com.yar.touchbanktask.util.NetworkUtil;

import rx.Observable;

public interface DataSourceProvider<T> extends NetworkUtil.NetworkStatusChangeListener {

    Observable<T> memory();

    Observable<T> disk();

    Observable<T> network();

    Observable<T> sink();
}
