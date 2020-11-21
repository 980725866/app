package com.hhtc.deathrecipient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private IMyAidlInterface mIMyAidlInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
            @Override
            public void binderDied() {
                Log.d(TAG, "binderDied");
            }
        };

        ServiceConnection connection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, final IBinder service) {
                    mIMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
                    if (mIMyAidlInterface != null) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    mIMyAidlInterface.asBinder().linkToDeath(deathRecipient, 0);
                                    mIMyAidlInterface.sendMsg("hello world");
                                    // 这里一定要是Stub对象否则传不到服务端
                                    mIMyAidlInterface.registerLister(new IMyRegisterInterface.Stub() {
                                        @Override
                                        public void callBack(String msg) throws RemoteException {

                                        }
                                    });
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mIMyAidlInterface = null;
            }
        };

        bindService(new Intent(this, MyService.class), connection, Context.BIND_AUTO_CREATE);
    }
}
