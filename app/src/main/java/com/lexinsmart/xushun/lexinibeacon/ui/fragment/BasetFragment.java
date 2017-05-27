package com.lexinsmart.xushun.lexinibeacon.ui.fragment;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.PersistableBundle;

import com.lexinsmart.xushun.lexinibeacon.MyApplication;
import com.orhanobut.logger.Logger;
import com.polidea.rxandroidble.RxBleClient;

/**
 * Created by xushun on 2017/5/25.
 */

public abstract class BasetFragment extends BlePermissionsFragment {
    private final static int REQUEST_ENABLE_BT = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected RxBleClient getAppClient(){
        return ((MyApplication) getActivity().getApplication()).getRxBleClient();
    }

    /**
     * Handle error on UiThread
     * @param message String
     */
    protected void handleSnackMessage(String message){
        Runnable runnable = () ->  {Logger.d(message);
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        };
        runnable.run();
    }

}
