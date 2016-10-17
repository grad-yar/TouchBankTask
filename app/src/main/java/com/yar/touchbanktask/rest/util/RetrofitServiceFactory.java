package com.yar.touchbanktask.rest.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.yar.touchbanktask.BuildConfig;
import com.yar.touchbanktask.entity.realm.RealmString;
import com.yar.touchbanktask.util.RealmGsonConverter;

import io.realm.RealmList;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitServiceFactory {

    private static OkHttpClient.Builder sOkClientBuilder = new OkHttpClient.Builder();
    private static Retrofit.Builder sRetrofitBuilder = new Retrofit.Builder();

    public static <T> T makeService(Class<T> serviceClass, String baseUrl, String token) {

        if (token != null && !token.isEmpty()) {

            Interceptor tokenInterceptor = chain -> {
                Request request = chain.request();

                HttpUrl url = request.url().newBuilder()
                        .addQueryParameter("access_token", token)
                        .build();

                Request modifiedRequest = request.newBuilder()
                        .url(url)
                        .build();

                return chain.proceed(modifiedRequest);
            };

            sOkClientBuilder.addInterceptor(tokenInterceptor);
        }

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            sOkClientBuilder.addInterceptor(loggingInterceptor);
        }

        OkHttpClient okHttpClient = sOkClientBuilder.build();

        Gson gson = new GsonBuilder().registerTypeAdapter(
                new TypeToken<RealmList<RealmString>>() {
                }.getType(), new RealmGsonConverter()).create();

        Retrofit retrofitService = sRetrofitBuilder.baseUrl(baseUrl).client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofitService.create(serviceClass);
    }
}
