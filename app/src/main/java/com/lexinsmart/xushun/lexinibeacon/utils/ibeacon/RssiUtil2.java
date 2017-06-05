package com.lexinsmart.xushun.lexinibeacon.utils.ibeacon;

/**
 * 功能：根据rssi计算距离
 * Created by liuhuichao on 2017/1/17.
 */

public class RssiUtil2 {
    //A和n的值，需要根据实际环境进行检测得出
    private static final double A_Value = 66;/**A - 发射端和接收端相隔1米时的信号强度*/
    private static final double n_Value=2.52;/** n - 环境衰减因子*/

    /**
     * 根据Rssi获得返回的距离,返回数据单位为m
     * @param rssi
     * @return
     */
    public static double getDistance(int rssi){
        int iRssi = Math.abs(rssi);
        double power = (iRssi-A_Value)/(10*n_Value);
        return Math.pow(10,power);
    }

    static int FIFO_NUM = 10;
    static int s_chIx = 0;
    static boolean s_chIsFull = false;
    static  int[] s_achBuf = new int[FIFO_NUM];
    public static int Filter(int chVal){
        chVal = Math.abs(chVal);
        int  nCnt;
        int nSum;
        int chMinVal;
        int chMaxVal;
        int chTemp;

		 /* 保存新值，剔除旧值*/
        s_achBuf[s_chIx] = chVal;
        if (++s_chIx >= FIFO_NUM)
        {
            s_chIx = 0;    /* Wrap to 1th unit */
            s_chIsFull = true;
        }

		  /* Number of sampled data less than N */
        if (!s_chIsFull)
        {
            nSum = 0;
            for (nCnt = 0; nCnt < s_chIx; ++nCnt)
            {
                nSum += s_achBuf[nCnt];
            }

            return (int)(nSum / s_chIx);
        }

		  /* Get the SUM and Max. and Min. */
        chMaxVal =0;
        chMinVal = s_achBuf[0];
        ;
        nSum = 0;
        for (nCnt = 0; nCnt < FIFO_NUM; ++nCnt)
        {
            chTemp = s_achBuf[nCnt];
            nSum += chTemp;
            if (chTemp > chMaxVal)
            {
                chMaxVal = chTemp;
            }
            else if (chTemp < chMinVal)
            {
                chMinVal = chTemp;
            }
        }

        System.out.print("sum:\t"+nSum+"\t   ----");
		  /* Calculate the average */
        nSum -= (chMaxVal + chMinVal);   /* SUB Max. and Min. */
        System.out.print(nSum+"\t---");
        System.out.print("\t max:"+chMaxVal+"\t---");
        System.out.print("\t min:"+chMinVal+"\t---");



        nSum /= (FIFO_NUM - 2);    /* Get average */
        System.out.print(nSum+"\t---");
        System.out.println();

        return (int)nSum;
    }
}