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
import com.lexinsmart.xushun.lexinibeacon.model.BasesBean;
import com.lexinsmart.xushun.lexinibeacon.model.DeviceInfo;
import com.lexinsmart.xushun.lexinibeacon.ui.adapter.DeviceListAdapter;
import com.lexinsmart.xushun.lexinibeacon.ui.views.PositionBg;
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

public class DeviceFragment extends BasetFragment {

    static Context mContext;

    BasesBean basesBean = new BasesBean();
    List<BasesBean.BaseBean> base = new ArrayList<>();
    PositionBg mPositionBg;
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
                basesBean.getBase().get(0).setR(basesBean.getBase().get(0).getR()+1);
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

    @Override
    protected void appBluetoothReady(boolean ready, int status) {


    }

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

    }
}
