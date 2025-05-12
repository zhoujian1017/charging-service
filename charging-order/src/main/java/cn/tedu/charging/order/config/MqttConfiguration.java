package cn.tedu.charging.order.config;

import cn.tedu.charging.order.mqtt.MqttConsumer;
import cn.tedu.charging.order.mqtt.MqttContext;
import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mqtt的消息中间件 EMQX 客户端配置
 */
@Configuration
public class MqttConfiguration {

    /**
     * 用户名 这些配置类的信息 应该放到 配置文件 或者
     * nacos 中 (nacos 提供了服务注册和配置管理)
     *
     *
     */
    private String userName = "admin";

    /**
     * 密码
     */
    private String password = "public";

    /**
     * 连接地址 tcp协议 端口 1883
     */
    private String host = "tcp://localhost:1883";

    private String clientId = "order_service_client1";

    @Autowired
    private MqttContext mqttContext;

    @Bean
    public MqttClient mqttClient() throws Exception {
        //通过连接地址和客户端id和emqx进行连接
        //客户端id 目的是要告诉我们的服务器 谁在连接它
        MqttClient mqttClient = new MqttClient(host, clientId);
        //连接相关的配置
        MqttConnectOptions conOpts = new MqttConnectOptions();
        //配置用户名
        conOpts.setUserName(userName);
        //配置密码
        conOpts.setPassword(password.toCharArray());
        //自动重连
        conOpts.setAutomaticReconnect(true);
        //版本号设置
        conOpts.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        //清理会话session
        conOpts.setCleanSession(true);
        //设置mqtt回调类 MqttConsumer
        mqttClient.setCallback(new MqttConsumer(mqttContext,mqttClient));
        //基于上面配置的 conOpts 进行连接
        mqttClient.connect(conOpts);

        return mqttClient;
    }

}