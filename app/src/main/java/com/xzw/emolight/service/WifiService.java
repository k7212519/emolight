package com.xzw.emolight.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.renderscript.ScriptGroup;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.BreakIterator;

import static com.xzw.emolight.service.WifiService.ACTION_SEND_MSG;

public class WifiService extends Service {

    public final static int ACTION_SEND_MSG = 1;
    public final static String ACTION_BR_SEND_MSG = "WifiService.Action.SendMsg";

    public Handler handlerThread;
    private Socket mSocket;
    private String mIpAddress;
    private int mClientPort;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    public WifiService() {
        this.mIpAddress = "192.168.43.249";
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

                //子线程接收消息
                Looper.prepare();
                handlerThread = new Handler(new Handler.Callback(){
                    @Override
                    public boolean handleMessage(Message msg) {
                        msg.obj.toString();
                        switch (msg.what) {
                            case ACTION_SEND_MSG:
                                //发送消息
                                writeMsg(msg.obj.toString());
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                Looper.loop();
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
     * 该方法在主线程执行接受不到消息（原因未知）
     * 放在一个子线程执行可以避免每次发送都需要创建新的线程
     */
    public void writeMsg(final String msg) {
        Log.d("WifiDebug", "writeMsg");
        Log.d("WifiDebug", msg);
        if (msg.length() == 0 || mOutputStream == null)
            return;
        try {   //发送
            mOutputStream.write(msg.getBytes());
            mOutputStream.flush();
            } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        //向子线程发送消息
        handlerThread.obtainMessage(ACTION_SEND_MSG, msg).sendToTarget();
    }

    class BroadcastReceiverInService extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String msg = intent.getStringExtra("msg");
            Log.d("WifiDebug", action);
            Log.d("WifiDebug", "receivedBRinService");
            switch (action) {
                case ACTION_BR_SEND_MSG:
                    sendMsg(msg);
                    break;
                default:
                    break;
            }
        }
    }
}
