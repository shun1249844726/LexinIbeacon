package com.lexinsmart.xushun.lexinibeacon.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.lexinsmart.xushun.lexinibeacon.R;
import com.lexinsmart.xushun.lexinibeacon.model.BaseStationBean;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

/**
 * Created by xushun on 2017/5/24.
 */

public class ApplicationScenariosFragment extends Fragment {
    public static ApplicationScenariosFragment newInstance(String info){
        Bundle args = new Bundle();
        ApplicationScenariosFragment applicationScenariosFragment = new ApplicationScenariosFragment();
        args.putString("info", info);
        applicationScenariosFragment.setArguments(args);
        return applicationScenariosFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fl_application_scenarios,null);
        Button mButton = (Button) view.findViewById(R.id.button_testtt);
        mButton.setText(getArguments().get("info").toString());





        return view;
    }
}
