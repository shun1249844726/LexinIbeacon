package com.lexinsmart.xushun.lexinibeacon.ui.fragment;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * Created by xushun on 2017/5/25.
 */

public abstract class BlePermissionsFragment extends RxFragment{

    private static final int REQUEST_PERMISSION_COARSE = 1;

    public static final int REQUEST_SETTINGS_BLUETOOTH = 2;
    public static final int REQUEST_SETTINGS_LOCATION = 3;

    public static final int REQUIREMENTS_OK = -1;
    public static final int REQUIREMENTS_ISSUE_PERMISSION_NOT_ALLOWED = 0;
    public static final int REQUIREMENTS_ISSUE_SETTINGS_LOCATION_NOT_ACTIVATED = 1;
    public static final int REQUIREMENTS_ISSUE_SETTINGS_BLUETOOTH_NOT_ACTIVATED = 2;
    public static final int REQUIREMENTS_ISSUE_SETTINGS_LOCATION_AND_BLUETOOTH_NOT_ACTIVATED = 3;

    static Context mContext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.mContext = context;
    }
    @Override
    public void onResume() {
        super.onResume();
        stepCheckPermission();
        getActivity().registerReceiver(mBluetoothReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        getActivity().registerReceiver(mGpsReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
    }



    @Override
    public void onPause() {
        getActivity().unregisterReceiver(mBluetoothReceiver);
        getActivity().unregisterReceiver(mGpsReceiver);
        super.onPause();
    }

    /**
     * AppBluetoothReady - Called in onResume after "stepCheckPermission"
     */
    protected abstract void appBluetoothReady(boolean ready, int status);


    /**
     * Check permisssion
     */
    private void stepCheckPermission(){
        boolean permissionAllowed = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
        if (!permissionAllowed) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSION_COARSE);
        }else{
            stepCheckAllStates();
        }
    }

    /**
     * Check location and bluetooth
     */
    private void stepCheckAllStates() {
        boolean gps = checkGPSEnabled();
        boolean bluetooth = checkBluetoothEnabled();
        boolean permissionAllowed = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        if(!permissionAllowed){
            appBluetoothReady(false, REQUIREMENTS_ISSUE_PERMISSION_NOT_ALLOWED);
        }else if(gps && bluetooth){
            appBluetoothReady(true, REQUIREMENTS_OK);
        }else if(!gps && !bluetooth){
            appBluetoothReady(false, REQUIREMENTS_ISSUE_SETTINGS_LOCATION_AND_BLUETOOTH_NOT_ACTIVATED);
        }else if(gps && !bluetooth){
            appBluetoothReady(false, REQUIREMENTS_ISSUE_SETTINGS_BLUETOOTH_NOT_ACTIVATED);
        }else if(!gps && bluetooth){
            appBluetoothReady(false, REQUIREMENTS_ISSUE_SETTINGS_LOCATION_NOT_ACTIVATED);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_COARSE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    stepCheckPermission();
                } else {
                    appBluetoothReady(false, REQUIREMENTS_ISSUE_PERMISSION_NOT_ALLOWED);
                }
                return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_SETTINGS_BLUETOOTH:
                stepCheckAllStates();
                break;
            case REQUEST_SETTINGS_LOCATION:
                stepCheckAllStates();
                break;
        }
    }

    /**
     * Check Bluetooth enabled
     * @return boolean result
     */
    private boolean checkBluetoothEnabled() {
        boolean bluetoothEnabled = false;
        BluetoothManager manager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter mBluetoothAdapter = manager.getAdapter();
        if(mBluetoothAdapter!=null && mBluetoothAdapter.isEnabled()){
            bluetoothEnabled = true;
        }
        return bluetoothEnabled;
    }


    /**
     * Check GPS enabled
     * @return boolean result
     */
    private boolean checkGPSEnabled() {
        boolean res = true;
        LocationManager lm = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            res = false;
        }
        return res;
    }


    /**
     * If bluetooth set to OFF/ON
     */
    private final BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
        public void onReceive (Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_ON
                        || intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF){
                    stepCheckAllStates();
                }
            }
        }
    };

    /**
     * If GPS set to OFF/ON
     */
    private final BroadcastReceiver mGpsReceiver = new BroadcastReceiver() {
        public void onReceive (Context context, Intent intent) {
            String action = intent.getAction();
            if(LocationManager.PROVIDERS_CHANGED_ACTION.equals(action)){
                stepCheckAllStates();
            }
        }
    };
}
