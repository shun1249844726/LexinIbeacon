package com.lexinsmart.xushun.lexinibeacon.model;

/**
 * Created by xushun on 2017/5/24.
 */

public class DeviceInfo {

    /*

    {
        "prefix": "dddd",
        "deviceName": "iBeacon_94E159ddddd",
        "major": "ddd",
        "minor": "minor",
        "mac": "f8f8f8f8f8f8f8f8f88",
        "uuid": "8d8f8f88fa88f8f8",
        "power": "40",
        "rssi": "-70"
    }
     */

    /**
     * prefix : dddd
     * deviceName : iBeacon_94E159ddddd
     * major : ddd
     * minor : minor
     * mac : f8f8f8f8f8f8f8f8f88
     * uuid : 8d8f8f88fa88f8f8
     * power : 40
     * rssi : -70
     */

    private String prefix;
    private String deviceName;
    private String major;
    private String minor;
    private String mac;
    private String uuid;
    private String power;
    private Integer rssi;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
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

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public Integer getRssi() {
        return rssi;
    }

    public void setRssi(Integer rssi) {
        this.rssi = rssi;
    }
}
