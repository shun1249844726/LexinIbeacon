package com.lexinsmart.xushun.lexinibeacon.utils.ibeacon;

import com.lexinsmart.xushun.lexinibeacon.model.DeviceInfo;
import com.lexinsmart.xushun.lexinibeacon.model.Round;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
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
    public static ArrayList<Round> sortRound(ArrayList<Round> rounds) {

        Round roundTemp = new Round(0, 0, 0);
        for (int i = 0; i < rounds.size() - 1; i++) {
            for (int j = 0; j < rounds.size() - 1 - i; j++) {
                Round roundj = rounds.get(j);
                Round roundj1 = rounds.get(j + 1);
                if (roundj.getR() > roundj1.getR()) {
                    roundTemp = roundj;
                    rounds.set(j, roundj1);
                    rounds.set(j + 1, roundTemp);
                }
            }
        }
        return rounds;
    }
}
