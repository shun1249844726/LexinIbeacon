package com.lexinsmart.xushun.lexinibeacon.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
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

import com.alibaba.fastjson.JSON;
import com.lexinsmart.xushun.lexinibeacon.R;
import com.lexinsmart.xushun.lexinibeacon.model.BaseStationBean;
import com.lexinsmart.xushun.lexinibeacon.model.BasesBean;
import com.lexinsmart.xushun.lexinibeacon.model.Coordinate;
import com.lexinsmart.xushun.lexinibeacon.model.DeviceInfo;
import com.lexinsmart.xushun.lexinibeacon.model.Round;
import com.lexinsmart.xushun.lexinibeacon.ui.adapter.DeviceListAdapter;
import com.lexinsmart.xushun.lexinibeacon.ui.views.PositionBg;
import com.lexinsmart.xushun.lexinibeacon.utils.file.FileUtils;
import com.lexinsmart.xushun.lexinibeacon.utils.ibeacon.Calculate;
import com.lexinsmart.xushun.lexinibeacon.utils.ibeacon.KalmanFilter;
import com.lexinsmart.xushun.lexinibeacon.utils.ibeacon.MyCalculate;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;

/**
 * Created by xushun on 2017/5/23.
 */

public class DeviceFragment extends BasetFragment {

    static Context mContext;
    private Subscription scanSubscription;
    private List<DeviceInfo> mDeviceInfos;
    Map<String, Double> map = new HashMap<String, Double>();
    Map<String, Double> map2 = new HashMap<String, Double>();
    Map<String, Double> map3 = new HashMap<String, Double>();
    Map<String, Double> map4 = new HashMap<String, Double>();
    Map<String, Double> map5 = new HashMap<String, Double>();


    KalmanFilter myKalman = new KalmanFilter();
    KalmanFilter myKalman2 = new KalmanFilter();
    KalmanFilter myKalman3 = new KalmanFilter();

    boolean firstFlag = false;
    double initXhat = 0.0;
    double initP = 1.0;

    boolean firstFlag2 = false;
    double initXhat2 = 0.0;
    double initP2 = 1.0;


    boolean firstFlag3 = false;
    double initXhat3 = 0.0;
    double initP3 = 1.0;
    BasesBean basesBean = new BasesBean();
    List<BasesBean.BaseBean> base = new ArrayList<>();   //绘图的基站的信息
    PositionBg mPositionBg;   //绘图  基站

    public static DeviceFragment newInstance(String info) {
        Bundle args = new Bundle();
        DeviceFragment deviceFragment = new DeviceFragment();
        args.putString("info", info);
        deviceFragment.setArguments(args);
        return deviceFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fl_device_location, null);
        TextView tvInfo = (TextView) view.findViewById(R.id.tv_fl_device_title);


        mPositionBg = (PositionBg) view.findViewById(R.id.basesPosition);

        tvInfo.setText(getArguments().getString("info"));
        tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Don't click me.please!.", Snackbar.LENGTH_SHORT).show();
                basesBean.getBase().get(0).setR(basesBean.getBase().get(0).getR() + 1);
            }
        });

        initDatas();

        return view;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.mContext = context;
    }

    /**
     * 初始化数据
     */
    private void initDatas() {

        BasesBean.BaseBean baseBean = new BasesBean.BaseBean();
        baseBean.setBasemac("1");
        baseBean.setBasex(0);
        baseBean.setBasey(0);
        baseBean.setR(1);

        BasesBean.BaseBean baseBean2 = new BasesBean.BaseBean();
        baseBean2.setBasemac("2");
        baseBean2.setBasex(4.2);
        baseBean2.setBasey(0);
        baseBean2.setR(1);

        BasesBean.BaseBean baseBean3 = new BasesBean.BaseBean();
        baseBean3.setBasemac("3");
        baseBean3.setBasex(2.1);
        baseBean3.setBasey(2.1);
        baseBean3.setR(1);

        base.add(baseBean);
        base.add(baseBean2);
        base.add(baseBean3);
        basesBean.setBase(base);

        mPositionBg.setData(basesBean);


        mDeviceInfos = new ArrayList<DeviceInfo>();


    }

    @Override
    protected void appBluetoothReady(boolean ready, int status) {
        if (!ready) {
            handleSnackMessage("Bluetooth not ready");
        } else {


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

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 扫面结果处理
     *
     * @param rxBleScanResult
     */
    private void addDeviceIfNeeded(RxBleScanResult rxBleScanResult) {
        RxBleDevice bleDevice = rxBleScanResult.getBleDevice();

        if (bleDevice == null) {
            return;
        }
        String address = bleDevice.getMacAddress();
        DeviceInfo deviceInfo = new DeviceInfo();
        iBeaconClass.iBeacon ibeacon = iBeaconClass.fromScanData(rxBleScanResult.getBleDevice().getBluetoothDevice(), rxBleScanResult.getRssi(), rxBleScanResult.getScanRecord());
        if (ibeacon != null && ibeacon.rssi < 0 && ibeacon.rssi != -127) {


            switch (address) {
                case "78:A5:04:53:31:D0":

                    Logger.d("txpower:" + ibeacon.txPower);
//                    String filePath = Environment.getExternalStorageDirectory() + "/bluetoothdata.txt";
//                    String oldContent = FileUtils.readString(filePath, "utf-8");
//
//                    FileUtils.writeString(filePath, oldContent + ibeacon.rssi + "\t", "utf-8");

                    map.put("xhat", initXhat);
                    map.put("P", initP);
                    map.put("data", (double) ibeacon.rssi);
                    Map<String, Double> tempMap = myKalman.calc(map, firstFlag);
                    firstFlag = true;
                    DecimalFormat df = new DecimalFormat("######0"); //四色五入转换成整数
                    Logger.d("rssiD0:" + ibeacon.rssi + "\t" + df.format(tempMap.get("xhat")));
                    initP = tempMap.get("P");
                    initXhat = tempMap.get("xhat");
                    deviceInfo.setRssi(Integer.valueOf(df.format(initXhat)));


//                    String filePathKalman = Environment.getExternalStorageDirectory() + "/bluetoothdataKalman.txt";
//                    String oldContentKalman = FileUtils.readString(filePathKalman, "utf-8");
//                    FileUtils.writeString(filePathKalman, oldContentKalman + df.format(tempMap.get("xhat")) + "\t", "utf-8");
//
//                    String filePathAve = Environment.getExternalStorageDirectory() + "/bluetoothdataAve.txt";
//                    String oldContentAve = FileUtils.readString(filePathAve, "utf-8");
//                    FileUtils.writeString(filePathAve, oldContentAve + (-1)*rssiUtilD0.Filter(ibeacon.rssi) + "\t", "utf-8");


//                    Logger.d(address + "->\t" +ibeacon.rssi+ "\t"+  rssiUtilD0.Filter(ibeacon.rssi));
//                    deviceInfo.setRssi(rssiUtilD0.Filter(ibeacon.rssi));
                    break;
                case "78:A5:04:53:26:0C":
                    map2.put("xhat", initXhat2);
                    map2.put("P", initP2);
                    map2.put("data", (double) ibeacon.rssi);
                    Map<String, Double> tempMap2 = myKalman2.calc(map2, firstFlag2);
                    firstFlag2 = true;
                    DecimalFormat df2 = new DecimalFormat("######0"); //四色五入转换成整数
                    Logger.d("rssi0C:" + ibeacon.rssi + "\t" + df2.format(tempMap2.get("xhat")));
                    initP2 = tempMap2.get("P");
                    initXhat2 = tempMap2.get("xhat");
                    deviceInfo.setRssi(Integer.valueOf(df2.format(initXhat2)));


                    break;
                case "D0:39:72:BF:50:DF":
                    map3.put("xhat", initXhat3);
                    map3.put("P", initP3);
                    map3.put("data", (double) ibeacon.rssi);
                    Map<String, Double> tempMap3 = myKalman3.calc(map3, firstFlag3);
                    firstFlag3 = true;
                    DecimalFormat df3 = new DecimalFormat("######0"); //四色五入转换成整数
                    Logger.d("rssiDF:" + ibeacon.rssi + "\t" + df3.format(tempMap3.get("xhat")));
                    initP3 = tempMap3.get("P");
                    initXhat3 = tempMap3.get("xhat");
                    deviceInfo.setRssi(Integer.valueOf(df3.format(initXhat3)));


                    break;
//                case "D0:39:72:BF:4F:4E":
//
//                    break;
//                case "78:A5:04:53:1C:47":
//                    deviceInfo.setRssi(ibeacon.rssi);
//
//
//                    break;
//
//                case "19:18:FC:03:DA:BF":
//                    deviceInfo.setRssi(ibeacon.rssi);
//
//
//
//                    break;
//                case "98:7B:F3:5D:30:91":
//
//                    deviceInfo.setRssi(ibeacon.rssi);
//
//                    break;
//                case "5C:F8:21:DF:95:7F":
//                    deviceInfo.setRssi(ibeacon.rssi);
//
//                    break;

                default:
                    Logger.d("666");

                    deviceInfo.setRssi(-120);
                    break;
            }


            deviceInfo.setMac(bleDevice.getMacAddress());
            deviceInfo.setMajor("" + ibeacon.major);
            deviceInfo.setMinor("" + ibeacon.minor);
            deviceInfo.setPower(ibeacon.txPower);
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

    /**
     * 时间线程，对结果定时处理
     */
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


                    Collections.sort(mDeviceInfos, new Sorts());
                    for (int i = 0; i < mDeviceInfos.size(); i++) {
                        Logger.d("Mac:" + mDeviceInfos.get(i).getMac()
                                + "\t" + mDeviceInfos.get(i).getRssi()
                                + "\t" + mDeviceInfos.get(i).getPower());


                        Double distance = RssiUtil.getDistance(mDeviceInfos.get(i).getRssi(), mDeviceInfos.get(i).getPower());

                        switch (mDeviceInfos.get(i).getMac()) {

                            case "78:A5:04:53:26:0C":
                                basesBean.getBase().get(1).setBasemac("0C");
                                basesBean.getBase().get(1).setR(distance);
                                break;
                            case "D0:39:72:BF:50:DF":
                                basesBean.getBase().get(2).setBasemac("DF");
                                basesBean.getBase().get(2).setR(distance);
                                break;
                            case "78:A5:04:53:31:D0":

                                basesBean.getBase().get(0).setBasemac("D0");
                                basesBean.getBase().get(0).setR(distance);
                                break;
                            default:
                                break;
                        }

                    }

                    Round round1 = new Round(0, 0, basesBean.getBase().get(0).getR());
                    Round round2 = new Round(2.1, 2.4, basesBean.getBase().get(2).getR());
                    Round round3 = new Round(4.2, 0, basesBean.getBase().get(1).getR());

                    Calculate calculate = new Calculate();
                    Coordinate coordinate = calculate.triCentroid(round1, round2, round3);
                    System.out.println("result:\t" + coordinate.getX() + "\t" + coordinate.getY());


                    String filePath = Environment.getExternalStorageDirectory() + "/bluetoothdata.txt";
                    String oldContent = FileUtils.readString(filePath, "utf-8");

                    FileUtils.writeString(filePath, oldContent + coordinate.getX() + "\t" + coordinate.getY() + "\n", "utf-8");


                    mPositionBg.setData(basesBean);
                    mPositionBg.setLocationPoint(coordinate);

                    Logger.d("Mac:--------------------" + mDeviceInfos.size());
                    mDeviceInfos.clear();

                    break;

                default:
                    break;
            }
            scan_timer_select = (scan_timer_select + 1) % 4;
        }

    };

}
