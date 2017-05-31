package com.lexinsmart.xushun.lexinibeacon.utils.mqtt;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.ibm.micro.client.mqttv3.MqttDeliveryToken;
import com.ibm.micro.client.mqttv3.MqttMessage;
import com.ibm.micro.client.mqttv3.MqttTopic;

/**
 * Created by xushun on 2017/5/31.
 */




public class CallBack implements com.ibm.micro.client.mqttv3.MqttCallback {
    private String instanceData = "";
    private Handler handler;

    public CallBack(String instance, Handler handler) {
        instanceData = instance;
        this.handler = handler;
    }

    public void messageArrived(MqttTopic topic, MqttMessage message) {
        try {
            Message msg = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString("content", message.toString());
            msg.what = 2;
            msg.setData(bundle);
            handler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //下面两个方法交给你们自己去实现吧。😄
    @Override
    public void connectionLost(Throwable throwable) {

    }

    public void deliveryComplete(MqttDeliveryToken token) {

    }
}