package com.hhtc.deathrecipient;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

public class MyService extends Service {

    public static final String TAG = "MyService";

    private RemoteCallbackList<IMyRegisterInterface> mCallBacks = new RemoteCallbackList<>();

    private HandlerThread mHandlerThread;

    private Handler mHandler;

    public MyService() {
        Log.d(TAG, "MyService");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandlerThread = new HandlerThread("MyService");
        mHandler = new Handler(mHandlerThread.getLooper());
        mHandler.sendEmptyMessageAtTime(MyHandler.GET_INFO, 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        mHandlerThread.quit();
    }

    @Override
    public IBinder onBind(Intent intent) {
       return new IMyAidlInterface.Stub() {

           @Override
           public void sendMsg(String msg) throws RemoteException {

           }

           @Override
           public void registerLister(IMyRegisterInterface register) throws RemoteException {
               if (register != null) {
                   Log.d(TAG, "registerLister");
                   mCallBacks.register(register);
               }
           }

           @Override
           public void unregisterLister(IMyRegisterInterface register) throws RemoteException {
               if (register != null) {
                   Log.d(TAG, "unregisterLister");
                   mCallBacks.unregister(register);
               }
           }
       };
    }

    private class MyHandler extends Handler {

        public static final int GET_INFO = 0;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_INFO:
                    Log.d(TAG, "size = " + mCallBacks.getRegisteredCallbackCount());
                    sendEmptyMessageAtTime(MyHandler.GET_INFO, 1000);
                    break;
                default:
                    break;

            }
        }
    }
}
