package com.lexinsmart.xushun.lexinibeacon.utils.ibeacon;

/**
 /* 卡尔曼滤波
 */
import java.util.HashMap;
import java.util.Map;

public class KalmanFilter {

    private final double Q = 0.00001;
    private final double R = 0.001;
    private double Z ;
    private double xhat;
    private double xhatminus;
    private double P;
    private double Pminus;
    private double K;

    Map<String,Double> resultMap = new HashMap<>();
    //
//    public MyKalman(HashMap<String,Double> map){
//
//    }
    public  Map<String,Double> calc(Map<String,Double> map,boolean flag){

        if (!flag){
            resultMap.put("xhat",map.get("xhat"));
            resultMap.put("data",map.get("data"));
            resultMap.put("P",map.get("P"));
            return resultMap;
        }
        Z = map.get("data");

        xhatminus = map.get("xhat");

        Pminus = map.get("P")+Q;

        K = Pminus/(Pminus + R);

        xhat = xhatminus+K*(Z - xhatminus);
        System.out.println("ddd:"+xhat+ "\t"+xhatminus+"\t"+K+"\t"+Z);

        P = (1-K)*Pminus;


        resultMap.put("xhat",xhat);
        resultMap.put("P",P);

        return resultMap;
    }

}
