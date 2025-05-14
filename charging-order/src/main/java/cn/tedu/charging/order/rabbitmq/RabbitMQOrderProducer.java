package cn.tedu.charging.order.rabbitmq;

import cn.tedu.charging.order.config.RabbitMQConfiguration;
import cn.tedu.charging.order.pojo.po.OrderMQPO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQOrderProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendOrder(OrderMQPO orderMQPO) {
        rabbitTemplate.convertAndSend(RabbitMQConfiguration.EXCHANGE_NAME,
                RabbitMQConfiguration.ROUTING_KEY, orderMQPO);
    }
}
