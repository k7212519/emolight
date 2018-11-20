package com.xzw.emolight.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.renderscript.ScriptGroup;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.BreakIterator;

public class WifiService extends Service {
    private Socket mSocket;
    private String mIpAddress;
    private int mClientPort;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    public WifiService() {
        this.mIpAddress = "192.168.4.1";
        this.mClientPort = 80;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //注册广播
        registerBroadcastReceiver();
        Log.d("WifiDebug", "onCreate");
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("WifiDebug", "onStart");
        Thread wifiSocketThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket = new Socket(mIpAddress, mClientPort);
                    if(mSocket != null){
                        //获取输出流、输入流
                        Log.d("WifiDebug", "获得了socket");
                        mOutputStream = mSocket.getOutputStream();
                        mInputStream = mSocket.getInputStream();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        wifiSocketThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("WifiDebug", "onDestroy");
    }

    /**
     * 注册广播
     */
    private void registerBroadcastReceiver() {
        BroadcastReceiverInService broadcastReceiverInService = new BroadcastReceiverInService();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("WifiService.Action.SendMsg");
        registerReceiver(broadcastReceiverInService, intentFilter);
    }

    /**
     * 向socket发送消息
     * @param msg
     */
    public void writeMsg(String msg){
        Log.d("WifiDebug", "writeMsg");
        Log.d("WifiDebug",msg);
        if(msg.length() == 0 || mOutputStream == null)
            return;
        try {   //发送
            mOutputStream.write(msg.getBytes());
            mOutputStream.flush();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    class BroadcastReceiverInService extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String msg = intent.getStringExtra("msg");
            Log.d("WifiDebug", action);
            Log.d("WifiDebug", "receivedBRinService");
            switch (action) {
                case "WifiService.Action.SendMsg":
                    writeMsg("100010000000a");
                    Log.d("WifiDebug", "writeFinish");
                    break;
                default:
                    break;
            }
        }
    }



}
