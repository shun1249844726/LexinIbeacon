package com.lexinsmart.xushun.lexinibeacon.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lexinsmart.xushun.lexinibeacon.R;
import com.lexinsmart.xushun.lexinibeacon.model.DeviceInfo;
import com.lexinsmart.xushun.lexinibeacon.ui.adapter.DeviceListAdapter;
import com.lexinsmart.xushun.lexinibeacon.utils.iBeaconClass;
import com.orhanobut.logger.Logger;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.RxBleScanResult;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * Created by xushun on 2017/5/25.
 */

public class ScanListFragment extends BasetFragment {

    DeviceListAdapter mAdapter;
    static Context mContext;
    private List<DeviceInfo> mDeviceInfos;

    private Subscription scanSubscription;


    public static ScanListFragment newInstance(String info) {
        Bundle args = new Bundle();
        ScanListFragment deviceFragment = new ScanListFragment();
        args.putString("info", info);
        deviceFragment.setArguments(args);
        return deviceFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fl_device_list, null);
        TextView tvInfo = (TextView) view.findViewById(R.id.tv_fl_devicelist);
        RecyclerView mRvDeviceScanList = (RecyclerView) view.findViewById(R.id.rv_device_scan_list);

        tvInfo.setText(getArguments().getString("info"));
        tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Don't click me.please!.", Snackbar.LENGTH_SHORT).show();
            }
        });


        initDatas();
        mAdapter = new DeviceListAdapter(mContext, mDeviceInfos);

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvDeviceScanList.setLayoutManager(linearLayoutManager);

        //设置适配器
        mRvDeviceScanList.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(sOnItemClickListener);


        return view;

    }

    private static DeviceListAdapter.OnItemClickListener sOnItemClickListener = new DeviceListAdapter.OnItemClickListener() {

        @Override
        public void onClick(int position) {
            TastyToast.makeText(mContext, "别碰我！", Toast.LENGTH_SHORT, TastyToast.DEFAULT).show();
        }
    };

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.mContext = context;
    }


    @Override
    protected void appBluetoothReady(boolean ready, int status) {
        if (!ready) {
            handleSnackMessage("Bluetooth not ready");
        } else {

            mDeviceInfos.clear();
            mAdapter.notifyDataSetChanged();

            search_timer.sendEmptyMessageDelayed(0, 500);

            //Start scan
            subscribeBle(true);
        }
    }

    private void subscribeBle(boolean bool) {
        if (bool) {
            //Start scan
            scanSubscription = getAppClient().scanBleDevices()
                    .subscribe(
                            rxBleScanResult -> {
                                addDeviceIfNeeded(rxBleScanResult);
                            },
                            throwable -> {
                                // Handle an error here.
                                handleSnackMessage("Error state connection : " + throwable.getMessage());
                            }
                    );
        } else {
            scanSubscription.unsubscribe();
        }

    }

    // 开始扫描
    private int scan_timer_select = 0;
    private boolean scan_flag = true;
    private Handler search_timer = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            search_timer.sendEmptyMessageDelayed(0, 500);
            if (!scan_flag) {
                return;
            }
            // 扫描时间调度
            switch (scan_timer_select) {
                case 1:
                    subscribeBle(true);
                    mDeviceInfos.clear();
                    break;

                case 3: // 停止扫描(结算)
                    subscribeBle(false);

//                    mDeviceInfosDis.clear();
//
//                    for (DeviceInfo deviceInfo : mDeviceInfos){
//                        mDeviceInfosDis.add(deviceInfo);
//                    }
//                    Logger.d("size:"+mDeviceInfosDis.size()  +" " +mDeviceInfos.size());
//

                    mAdapter.notifyDataSetChanged();
                    break;

                default:
                    break;
            }
            scan_timer_select = (scan_timer_select + 1) % 4;
        }

    };

    private void addDeviceIfNeeded(RxBleScanResult rxBleScanResult) {
        RxBleDevice bleDevice = rxBleScanResult.getBleDevice();

        if (bleDevice == null) {
            return;
        }
        String address = bleDevice.getMacAddress();
        DeviceInfo deviceInfo = new DeviceInfo();
        iBeaconClass.iBeacon ibeacon = iBeaconClass.fromScanData(rxBleScanResult.getBleDevice().getBluetoothDevice(), rxBleScanResult.getRssi(), rxBleScanResult.getScanRecord());
        if (ibeacon != null) {
            deviceInfo.setMac(bleDevice.getMacAddress());
            deviceInfo.setMajor("" + ibeacon.major);
            deviceInfo.setMinor("" + ibeacon.minor);
            deviceInfo.setRssi("" + ibeacon.rssi);
            deviceInfo.setDeviceName(bleDevice.getName());

            for (int i = 0; i < mDeviceInfos.size(); i++) {
                if (address.equals(mDeviceInfos.get(i).getMac())) {

         //           mDeviceInfos.add(i+1,deviceInfo);
                    mDeviceInfos.remove(i);
                    break;

                }
            }
            mDeviceInfos.add(deviceInfo);
        }

//        if (!listDevicesFound.contains(bleDevice)) {
//            DeviceInfo deviceInfo = new DeviceInfo();
//            iBeaconClass.iBeacon ibeacon = iBeaconClass.fromScanData(rxBleScanResult.getBleDevice().getBluetoothDevice(),rxBleScanResult.getRssi(),rxBleScanResult.getScanRecord());
//
//
//            if (ibeacon != null){
//                deviceInfo.setMac(bleDevice.getMacAddress());
//                deviceInfo.setMajor(""+ibeacon.major);
//                deviceInfo.setMinor(""+ibeacon.minor);
//                deviceInfo.setRssi(""+ibeacon.rssi);
//                deviceInfo.setDeviceName(bleDevice.getName());
//                mDeviceInfos.add(deviceInfo);
//                mAdapter.notifyDataSetChanged();
//            }
//        }
//        else {
//            iBeaconClass.iBeacon ibeacon = iBeaconClass.fromScanData(rxBleScanResult.getBleDevice().getBluetoothDevice(),rxBleScanResult.getRssi(),rxBleScanResult.getScanRecord());
//            if (ibeacon != null) {
//
//                for (int i = 0; i < mDeviceInfos.size(); i++) {
//                    if (0 == bleDevice.getMacAddress().compareTo(mDeviceInfos.get(i).getMac())) {
//                        mDeviceInfos.get(i).setRssi("" + ibeacon.rssi);
//                    }
//                }
//            }
//        }
    }

    private void initDatas() {

        mDeviceInfos = new ArrayList<DeviceInfo>();

//
//
//        for (int i=0;i<3;i++){
//            DeviceInfo deviceInfo_1 = new DeviceInfo();
//            deviceInfo_1.setMac("mac1");
//            deviceInfo_1.setMajor("major");
//            deviceInfo_1.setDeviceName("name"+i);
//            mDeviceInfos.add(deviceInfo_1);
//        }

    }
}
