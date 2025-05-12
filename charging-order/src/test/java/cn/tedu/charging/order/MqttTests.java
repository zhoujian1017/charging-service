package cn.tedu.charging.order;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MqttTests {

    @Autowired
    private MqttClient mqttClient;

    @Test
    public void testMqttClient() throws MqttException {
        String topic = "java";
        String message = "Java基础-基本语法-基本类型111";
        mqttClient.publish(topic,message.getBytes(),0,true);
    }
}