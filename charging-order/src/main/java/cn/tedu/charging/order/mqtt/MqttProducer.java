package cn.tedu.charging.order.mqtt;

import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.C;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * emqx 消息发送客户端
 */
@Slf4j
@Component
public class MqttProducer {
    @Autowired
    private MqttClient mqttClient;

    /**
     * 通过mqttClient给EMQX服务器发送消息
     * @param topic
     * @param msg
     */
    public void send(String topic, String msg) {
        try {
            mqttClient.publish(topic, msg.getBytes(), 0, true);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}
