package com.yar.touchbanktask.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

public class NetworkUtil {

    public static boolean isNetworkConnection(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();

    }

    public static NetworkStatusReceiver registerNetworkStatusReceiver(Context c) {
        NetworkStatusReceiver receiver = new NetworkStatusReceiver();

        c.getApplicationContext().registerReceiver(receiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        return receiver;
    }

    public interface NetworkStatusChangeListener {

        void onNetworkStatusChange(boolean available);

    }

    public static class NetworkStatusReceiver extends BroadcastReceiver {

        private List<NetworkStatusChangeListener> listeners = new ArrayList<>();


        @Override
        public void onReceive(Context context, Intent intent) {
            boolean connectionAvailable = !intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

            for (NetworkStatusChangeListener listener : listeners) {
                listener.onNetworkStatusChange(connectionAvailable);
            }
        }

        public void addListener(NetworkStatusChangeListener l) {
            listeners.add(l);
        }

        public void removeListener(NetworkStatusChangeListener l) {
            listeners.remove(l);
        }

        public void removeAllListeners() {
            listeners.clear();
        }

    }

}
