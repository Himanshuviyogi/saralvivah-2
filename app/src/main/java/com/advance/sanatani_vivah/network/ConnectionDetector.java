/**
 * @owner WyInDoks IT Service Private Limited
 * @author Hiren
 * Created on Dec 30, 2015
 */

package com.advance.sanatani_vivah.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Check Mobile internet connection availability
 * Created by Nasirali on 02-02-2019.
 */

public class ConnectionDetector {

    // check if internet is connected or not
    public static boolean isConnectingToInternet(Context context) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * if connectionType is set only wifi ( 2 )
     * then if wifi not connected then return false
     *
     * @param context
     * @param syncType
     * @return
     */
    public static boolean checkConnectionType(Context context, int syncType) {

        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (syncType == 2 && !mWifi.isConnected()) {
            return false;
        }
        return true;
    }
}
