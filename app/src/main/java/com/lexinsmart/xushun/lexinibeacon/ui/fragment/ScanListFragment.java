package com.lexinsmart.xushun.lexinibeacon.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lexinsmart.xushun.lexinibeacon.MyApplication;
import com.lexinsmart.xushun.lexinibeacon.R;
import com.lexinsmart.xushun.lexinibeacon.model.BaseStationBean;
import com.lexinsmart.xushun.lexinibeacon.model.DeviceInfo;
import com.lexinsmart.xushun.lexinibeacon.ui.adapter.DeviceListAdapter;
import com.lexinsmart.xushun.lexinibeacon.utils.file.FileUtils;
import com.lexinsmart.xushun.lexinibeacon.utils.ibeacon.KalmanFilter;
import com.lexinsmart.xushun.lexinibeacon.utils.ibeacon.RssiUtil;
import com.lexinsmart.xushun.lexinibeacon.utils.ibeacon.Sorts;
import com.lexinsmart.xushun.lexinibeacon.utils.ibeacon.iBeaconClass;
import com.lexinsmart.xushun.lexinibeacon.utils.mqtt.MqttV3Service;
import com.orhanobut.logger.Logger;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.RxBleScanResult;
import com.sdsmdg.tastytoast.TastyToast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscription;

import static com.lexinsmart.xushun.lexinibeacon.utils.file.SDCardUtils.isSDCardEnable;

/**
 * Created by xushun on 2017/5/25.
 */

public class ScanListFragment extends BasetFragment {

    DeviceListAdapter mAdapter;
    static Context mContext;
    private List<DeviceInfo> mDeviceInfos;

    private Subscription scanSubscription;
    RssiUtil rssiUtilD0 = new RssiUtil();
    Map<String, Double> map = new HashMap<String, Double>();
    KalmanFilter myKalman = new KalmanFilter();
    boolean firstFlag = false;
    double initXhat = 0.0;
    double initP = 1.0;


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
            new Thread(new MqttProcThread()).start();

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
                    //       subscribeBle(true);
                    //          mDeviceInfos.clear();
                    break;

                case 3: // 停止扫描(结算)
                    //       subscribeBle(false);


                    Collections.sort(mDeviceInfos, new Sorts());
                    mAdapter.notifyDataSetChanged();

                    int postSize = mDeviceInfos.size();

                    if (postSize > 5) {
                        postSize = 5;
                    }
                    BaseStationBean baseStationBean = new BaseStationBean();
                    ArrayList<BaseStationBean.BaseBean> baseBeenList = new ArrayList<>();
                    for (int i = 0; i < postSize; i++) {

                        BaseStationBean.BaseBean baseBean = new BaseStationBean.BaseBean();

                        baseBean.setMajor(mDeviceInfos.get(i).getMajor());
                        baseBean.setMinor(mDeviceInfos.get(i).getMinor());
                        baseBean.setRssi(mDeviceInfos.get(i).getRssi());
                        baseBean.setUuid(mDeviceInfos.get(i).getUuid());
                        baseBean.setMac(mDeviceInfos.get(i).getMac());
                        if (mDeviceInfos.get(i).getMac().equals("78:A5:04:53:1C:47")) {
                            Logger.d("rrrrsssi:" + mDeviceInfos.get(i).getRssi());
                        }

                        baseBeenList.add(baseBean);
                    }
                    baseStationBean.setBasenum(postSize);

                    baseStationBean.setBase(baseBeenList);
                    String jsonString = JSON.toJSONString(baseStationBean);
                    MqttV3Service.publishMsg(jsonString, 1, 0);

                    //                   Logger.json(jsonString);

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
        if (ibeacon != null && ibeacon.rssi < 0) {
            switch (address) {
                case "78:A5:04:53:31:D0":

                    deviceInfo.setRssi(ibeacon.rssi);

//                    Logger.d(address + "->\t" +ibeacon.rssi+ "\t"+  rssiUtilD0.Filter(ibeacon.rssi));
//                    deviceInfo.setRssi(rssiUtilD0.Filter(ibeacon.rssi));
                    break;
                case "78:A5:04:53:26:0C":
                    deviceInfo.setRssi(ibeacon.rssi);

                    break;
                case "D0:39:72:BF:4F:4E":
                    deviceInfo.setRssi(ibeacon.rssi);


                    break;
                case "78:A5:04:53:1C:47":
                    String filePath = Environment.getExternalStorageDirectory() + "/bluetoothdata.txt";
                    String oldContent = FileUtils.readString(filePath, "utf-8");

                    FileUtils.writeString(filePath, oldContent + ibeacon.rssi + "\t", "utf-8");

                    map.put("xhat", initXhat);
                    map.put("P", initP);
                    map.put("data", (double) ibeacon.rssi);
                    Map<String, Double> tempMap = myKalman.calc(map, firstFlag);
                    firstFlag = true;
                    DecimalFormat df = new DecimalFormat("######0"); //四色五入转换成整数
                    Logger.d("rssi:" + ibeacon.rssi + "\t" + df.format(tempMap.get("xhat")));
                    initP = tempMap.get("P");
                    initXhat = tempMap.get("xhat");
                    deviceInfo.setRssi(Integer.valueOf(df.format(initXhat)));


                    String filePathKalman = Environment.getExternalStorageDirectory() + "/bluetoothdataKalman.txt";
                    String oldContentKalman = FileUtils.readString(filePathKalman, "utf-8");
                    FileUtils.writeString(filePathKalman, oldContentKalman + df.format(tempMap.get("xhat")) + "\t", "utf-8");

                    String filePathAve = Environment.getExternalStorageDirectory() + "/bluetoothdataAve.txt";
                    String oldContentAve = FileUtils.readString(filePathAve, "utf-8");
                    FileUtils.writeString(filePathAve, oldContentAve + (-1)*rssiUtilD0.Filter(ibeacon.rssi) + "\t", "utf-8");


                    break;
                case "D0:39:72:BF:50:DF":
                    deviceInfo.setRssi(ibeacon.rssi);


                    break;
                default:
                    Logger.d("666");

                    deviceInfo.setRssi(-100);
                    break;
            }


            if (ibeacon.bluetoothAddress.equals("D0:39:72:BF:4F:4E")) {
                Logger.d("ibeacon:" + ibeacon.bluetoothAddress + "     " + ibeacon.rssi);
            }

            deviceInfo.setMac(bleDevice.getMacAddress());
            deviceInfo.setMajor("" + ibeacon.major);
            deviceInfo.setMinor("" + ibeacon.minor);
            deviceInfo.setDeviceName(bleDevice.getName());
            deviceInfo.setUuid(ibeacon.proximityUuid);

            for (int i = 0; i < mDeviceInfos.size(); i++) {
                if (address.equals(mDeviceInfos.get(i).getMac())) {
                    mDeviceInfos.remove(i);
                    break;
                }
            }
            mDeviceInfos.add(deviceInfo);
        }
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


    public class MqttProcThread implements Runnable {

        int randomid = (int) Math.floor(10000 + Math.random() * 90000);

        @Override
        public void run() {
            Message msg = new Message();
            ArrayList<String> topicList = new ArrayList<String>();
            topicList.add("ddd");
            boolean ret = MqttV3Service.connectionMqttServer(myHandler, MyApplication.ADDRESS, MyApplication.PORT, "lexin", topicList);
            if (ret) {
                msg.what = 1;
            } else {
                msg.what = 0;
            }
            msg.obj = "strresult";
            myHandler.sendMessage(msg);
        }
    }

    @SuppressWarnings("HandlerLeak")
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Toast.makeText(mContext, "连接成功", Toast.LENGTH_SHORT).show();

            } else if (msg.what == 0) {
                Toast.makeText(mContext, "连接失败", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 2) {
                String strContent = "";
                strContent += msg.getData().getString("content");
                System.out.println("strcontent:" + strContent);
            } else if (msg.what == 3) {
                if (MqttV3Service.closeMqtt()) {
                    Toast.makeText(mContext, "断开连接", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
