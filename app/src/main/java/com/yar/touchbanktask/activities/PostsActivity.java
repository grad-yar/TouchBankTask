package com.yar.touchbanktask.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.hawk.Hawk;
import com.yar.touchbanktask.InstagramApp;
import com.yar.touchbanktask.R;
import com.yar.touchbanktask.adapters.MediaItemAdapter;
import com.yar.touchbanktask.entity.json.media.RecentMedia;
import com.yar.touchbanktask.entity.json.user_info.Counts;
import com.yar.touchbanktask.entity.json.user_info.UserInfo;
import com.yar.touchbanktask.model.UserModel;
import com.yar.touchbanktask.util.NetworkUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class PostsActivity extends AppCompatActivity {

    @BindView(R.id.swipe_refresh_layout)
    protected SwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.posts_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;

    @BindView(R.id.tv_profile)
    TextView mTvProfile;

    @BindView(R.id.tv_posts)
    TextView mTvPosts;

    @BindView(R.id.tv_followers)
    TextView mTvFolowers;

    @BindView(R.id.tv_following)
    TextView mTvFollowing;

    @BindView(R.id.tv_full_name)
    TextView mTvFullName;

    private MediaItemAdapter mAdapter;

    private UserModel mUserModel;

    private CompositeSubscription mSubscription;

    private static final long POLL_INTERVAL = 5;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    private static final String TAG = PostsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String token = Hawk.get(LoginActivity.TOKEN_KEY);

        if (token == null || token.isEmpty()) {
            InstagramApp.instance().logOut();
            finish();
        }

        setContentView(R.layout.activity_posts);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mSubscription = new CompositeSubscription();

        mAdapter = new MediaItemAdapter(this);

        mUserModel = InstagramApp.instance().getUserModel();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefresh.setOnRefreshListener(this::refreshRecentMedia);

        if (savedInstanceState == null) {
            refreshRecentMedia();
        }
    }

    private void updateUserInfoView(UserInfo userInfo) {
        mTvProfile.setText(userInfo.getUsername());

        Counts counts = userInfo.getCounts();
        mTvPosts.setText(String.format(getString(R.string.tv_posts_text), counts.getMedia()));
        mTvFolowers.setText(String.format(getString(R.string.tv_followers_text), counts.getFollowedBy()));
        mTvFollowing.setText(String.format(getString(R.string.tv_following_text), counts.getFollows()));

        mTvFullName.setText(userInfo.getFullName());


        Glide.with(this).load(userInfo.getProfilePicture()).into(mIvAvatar);
    }

    private void updateRecentMediaView(RecentMedia recentMedia) {
        mAdapter.setMediaItems(recentMedia.getMediaItems());
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    private void refreshRecentMedia() {
        if (NetworkUtil.isNetworkConnection(this)) {
            mSubscription.add(
                    mUserModel.getFreshRecentMedia().subscribe(this::updateRecentMediaView,
                            throwable -> {
                                mSwipeRefresh.setRefreshing(false);
                                Log.e(TAG, throwable.getMessage());
                            }));
        } else {
            mSwipeRefresh.setRefreshing(false);
            Snackbar.make(mRecyclerView, R.string.message_no_connection, Snackbar.LENGTH_SHORT).show();
        }
    }

    private <T> Observable<T> pollingObservable(Observable<T> observableToPoll, long pollInterval, TimeUnit timeUnit) {
        return Observable.interval(0, pollInterval, timeUnit)
                .flatMap(aLong -> observableToPoll)
                .onErrorResumeNext(
                        throwable -> {
                            Log.e(TAG, throwable.toString());

                            return Observable.empty();
                        }
                )
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    protected void onStart() {

        mSubscription.add(
                pollingObservable(mUserModel.getRecentMedia(), POLL_INTERVAL, TIME_UNIT)
                        .subscribe(this::updateRecentMediaView)
        );

        mSubscription.add(
                pollingObservable(mUserModel.getUserInfo(), POLL_INTERVAL, TIME_UNIT)
                        .subscribe(this::updateUserInfoView)
        );

        super.onStart();
    }

    @Override
    protected void onStop() {

        mSubscription.clear();

        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_posts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sign_out) {
            InstagramApp.instance().logOut();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
