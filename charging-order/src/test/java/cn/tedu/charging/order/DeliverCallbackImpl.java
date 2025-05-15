package cn.tedu.charging.order;

import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;

public class DeliverCallbackImpl implements DeliverCallback {

    @Override
    public void handle(String consumerTag, Delivery delivery) throws IOException {
        byte[] body = delivery.getBody();
        String message = new String(body);
        System.out.println("消费消息" + message);
    }

}
