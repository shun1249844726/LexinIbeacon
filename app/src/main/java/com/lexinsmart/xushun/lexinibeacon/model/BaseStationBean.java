package com.lexinsmart.xushun.lexinibeacon.model;

import java.util.List;

/**
 * Created by xushun on 2017/6/1.
 */

public class BaseStationBean {
    /**
     * basenum : 5
     * base : [{"mac":"222222","uuid":"nsfewfiewfoew","major":"dmcoiwjc","minor":"sdnoiwj","rssi":123},{"mac":"222222","uuid":"nsfewfiewfoew","major":"dmcoiwjc","minor":"sdnoiwj","rssi":123},{"mac":"222222","uuid":"nsfewfiewfoew","major":"dmcoiwjc","minor":"sdnoiwj","rssi":123},{"mac":"222222","uuid":"nsfewfiewfoew","major":"dmcoiwjc","minor":"sdnoiwj","rssi":123},{"mac":"222222","uuid":"nsfewfiewfoew","major":"dmcoiwjc","minor":"sdnoiwj","rssi":123}]
     */

    private int basenum;
    private List<BaseBean> base;

    public int getBasenum() {
        return basenum;
    }

    public void setBasenum(int basenum) {
        this.basenum = basenum;
    }

    public List<BaseBean> getBase() {
        return base;
    }

    public void setBase(List<BaseBean> base) {
        this.base = base;
    }

    public static class BaseBean {
        /**
         * mac : 222222
         * uuid : nsfewfiewfoew
         * major : dmcoiwjc
         * minor : sdnoiwj
         * rssi : 123
         */

        private String mac;
        private String uuid;
        private String major;
        private String minor;
        private int rssi;

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getMajor() {
            return major;
        }

        public void setMajor(String major) {
            this.major = major;
        }

        public String getMinor() {
            return minor;
        }

        public void setMinor(String minor) {
            this.minor = minor;
        }

        public int getRssi() {
            return rssi;
        }

        public void setRssi(int rssi) {
            this.rssi = rssi;
        }
    }
}
