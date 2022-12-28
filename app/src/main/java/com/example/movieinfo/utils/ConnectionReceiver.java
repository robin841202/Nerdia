package com.example.movieinfo.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionReceiver extends BroadcastReceiver {
    // initialize listener
    public static ReceiverListener listener;

    @Override
    public void onReceive(Context context, Intent intent) {

        // initialize connectivity manager
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Initialize network info
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // check condition
        if (listener != null) {

            // when connectivity receiver
            // listener not null
            // get connection status
            boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

            // call listener method
            listener.onNetworkChange(isConnected);
        }
    }

    public interface ReceiverListener {
        // create method
        void onNetworkChange(boolean isConnected);
    }
}