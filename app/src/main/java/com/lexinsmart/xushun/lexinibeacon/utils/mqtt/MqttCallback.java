package com.lexinsmart.xushun.lexinibeacon.utils.mqtt;

import com.ibm.micro.client.mqttv3.MqttDeliveryToken;
import com.ibm.micro.client.mqttv3.MqttMessage;
import com.ibm.micro.client.mqttv3.MqttTopic;

/**
 * Created by xushun on 2017/5/31.
 */
public interface MqttCallback {
    void connectionLost(Throwable var1);

    void messageArrived(MqttTopic var1, MqttMessage var2) throws Exception;

    void deliveryComplete(MqttDeliveryToken var1);
}