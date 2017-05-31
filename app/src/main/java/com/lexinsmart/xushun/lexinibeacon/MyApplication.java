package com.lexinsmart.xushun.lexinibeacon;

import android.app.Application;

import com.orhanobut.logger.Logger;
import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.internal.RxBleLog;

/**
 * Created by xushun on 2017/5/23.
 */

public class MyApplication extends Application {
    private String TAG = "LoginDemo";
    private RxBleClient rxBleClient;
    public static String ADDRESS = "180.76.179.148";
    public static String PORT = "1883";


    @Override
    public void onCreate() {
        super.onCreate();

        Logger.init(TAG);

        rxBleClient= RxBleClient.create(getApplicationContext());
        rxBleClient.setLogLevel(RxBleLog.DEBUG);

    }
    public RxBleClient getRxBleClient() {
        return rxBleClient;
    }

}
