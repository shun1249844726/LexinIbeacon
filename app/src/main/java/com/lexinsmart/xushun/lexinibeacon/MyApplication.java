package com.lexinsmart.xushun.lexinibeacon;

import android.app.Application;

import com.orhanobut.logger.Logger;

/**
 * Created by xushun on 2017/5/23.
 */

public class MyApplication extends Application {
    private String TAG = "LoginDemo";

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.init(TAG);

    }
}
