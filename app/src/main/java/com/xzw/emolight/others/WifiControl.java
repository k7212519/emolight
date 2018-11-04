package com.xzw.emolight.others;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WifiControl {
    private Context context;
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;

    public WifiControl(Context context) {
        this.context = context;
        this.wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        this.wifiInfo = wifiManager.getConnectionInfo();
    }

    /**
     * 打开wifi
     */
    public void OpenWifi() {
        context.startActivity(new Intent("android.net.wifi.PICK_WIFI_NETWORK"));
    }

    /**
     * 判断wifi是否已经连接
     * @return
     */
    public boolean isWifiConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            return true;
        } else{
            return false;
        }
    }
}
