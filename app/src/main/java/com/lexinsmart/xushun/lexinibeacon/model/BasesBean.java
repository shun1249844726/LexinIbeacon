package com.lexinsmart.xushun.lexinibeacon.model;

import java.util.List;

/**
 * Created by xushun on 2017/6/16.
 */

public class BasesBean {

    private List<BaseBean> base;

    public List<BaseBean> getBase() {
        return base;
    }

    public void setBase(List<BaseBean> base) {
        this.base = base;
    }

    public static class BaseBean {
        /**
         * basemac : 12345
         * basex : 1.23
         * basey : 3.33
         * r : 1.4
         */

        private String basemac;
        private double basex;
        private double basey;
        private double r;

        public String getBasemac() {
            return basemac;
        }

        public void setBasemac(String basemac) {
            this.basemac = basemac;
        }

        public double getBasex() {
            return basex;
        }

        public void setBasex(double basex) {
            this.basex = basex;
        }

        public double getBasey() {
            return basey;
        }

        public void setBasey(double basey) {
            this.basey = basey;
        }

        public double getR() {
            return r;
        }

        public void setR(double r) {
            this.r = r;
        }
    }
}
