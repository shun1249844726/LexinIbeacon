package com.lexinsmart.xushun.lexinibeacon.utils.ibeacon;

import com.lexinsmart.xushun.lexinibeacon.model.DeviceInfo;
import com.orhanobut.logger.Logger;

import java.util.Comparator;

/**
 * Created by xushun on 2017/5/31.
 */

public class Sorts implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        DeviceInfo deviceInfo1 = (DeviceInfo) o1;
        DeviceInfo deviceInfo2 = (DeviceInfo) o2;

        if (deviceInfo1.getRssi()>deviceInfo2.getRssi()){
            return -1;
        }else {
            return 1;
        }
    }
}
