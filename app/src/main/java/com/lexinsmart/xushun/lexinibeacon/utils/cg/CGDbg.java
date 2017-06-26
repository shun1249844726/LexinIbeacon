package com.lexinsmart.xushun.lexinibeacon.utils.cg;

/**
 * @author Bruce Yang
 * 用于打印调试~
 */
public class CGDbg {
    public static final boolean DEBUG_MODE = true;

    // 方便进行调试信息的输出，开关~
    public static void println(Object info) {
        if(DEBUG_MODE) {
            System.out.println(info);
        }
    }
    public static void print(Object info) {
        if(DEBUG_MODE) {
            System.out.print(info);
        }
    }
}
