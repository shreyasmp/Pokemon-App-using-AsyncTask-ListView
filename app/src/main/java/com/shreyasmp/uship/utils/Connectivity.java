package com.shreyasmp.uship.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by shreyasmp on 4/1/17.
 *
 *  Class to check for internet connectivity.
 */

public class Connectivity  {

    /**
     *
     * @param context
     * @return
     *
     * Method returns a boolean value to callback whether there is internet connection or not.
     * Since application is Network dependent and needs to be handled from app crash
     *
     */

    public static boolean isNetworkConnected(Context context) {

        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) && networkInfo.isConnected();
    }

}
