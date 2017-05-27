package com.lexinsmart.xushun.lexinibeacon.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.orhanobut.logger.Logger;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by xushun on 2017/5/23.
 */

public class DeviceFragment extends Fragment {

    DeviceListAdapter mAdapter;
    private List<Integer> mDatas;
    static Context mContext;
    private List<DeviceInfo> mDeviceInfos;

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

    private void initDatas() {

        mDeviceInfos = new ArrayList<DeviceInfo>();


        for (int i=0;i<3;i++){
            DeviceInfo deviceInfo_1 = new DeviceInfo();
            deviceInfo_1.setMac("mac1");
            deviceInfo_1.setMajor("major");
            deviceInfo_1.setDeviceName("name"+i);
            mDeviceInfos.add(deviceInfo_1);
        }

    }
}
