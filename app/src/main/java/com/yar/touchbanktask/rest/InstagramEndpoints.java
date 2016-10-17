package com.yar.touchbanktask.rest;

import com.yar.touchbanktask.entity.json.media.RecentMedia;
import com.yar.touchbanktask.entity.json.user_info.UserInfo;
import com.yar.touchbanktask.rest.util.RetrofitServiceFactory;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public class InstagramEndpoints {

    public static final String REDIRECT_URI = "https://grad-yar.com";
    private static final String CLIENT_ID = "d86f7433ccce42da9cb19367342f91d0";
    private static final String CLIENT_SECRET = "5a90a4e7bfb1405fa349c9891748bf97"; // not used, cause OAuth2 implicit :)

    public static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/" +
            "?client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&response_type=token";
    private static final String BASE_URL = "https://api.instagram.com/v1/";

    private static UserEndpoints sUserEndpoints;

    public static void initEndpoints(String token) {
        sUserEndpoints = RetrofitServiceFactory.makeService(UserEndpoints.class, BASE_URL, token);
    }

    public static UserEndpoints getUserEndpoints() {
        return sUserEndpoints;
    }

    public interface UserEndpoints {

        @GET("users/self/")
        Observable<UserInfo> getUserInfo();

        @GET("users/self/media/recent/")
        Observable<RecentMedia> getRecentMedia(@Query("count") int count,
                                               @Query("min_id") long minId,
                                               @Query("max_id") long maxId);

        @GET("users/self/media/recent/")
        Observable<RecentMedia> getRecentMedia();
    }
}
