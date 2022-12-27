package com.example.movieinfo.utils;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionHelper{
    public static boolean checkConnection(Context context, ConnectionReceiver.ReceiverListener listener){
        // initialize intent filter
        IntentFilter intentFilter = new IntentFilter();
        // add action
        intentFilter.addAction("android.new.conn.CONNECTIVITY_CHANGE");

        // register receiver
        context.registerReceiver(new ConnectionReceiver(), intentFilter);
        // Initialize listener
        ConnectionReceiver.listener = listener;

        // Initialize connectivity manager
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Initialize network info
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        // get connection status
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        return isConnected;
    }
}