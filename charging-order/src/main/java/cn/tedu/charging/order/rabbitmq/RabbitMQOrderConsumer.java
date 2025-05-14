package cn.tedu.charging.order.rabbitmq;

import cn.tedu.charging.order.config.RabbitMQConfiguration;
import cn.tedu.charging.order.dao.repository.OrderRepository;
import cn.tedu.charging.order.pojo.po.OrderMQPO;
import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.C;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * rabbit-mq 消费者
 * 消费的是 死信队列
 */
@Component
@Slf4j
public class RabbitMQOrderConsumer {

    @Autowired
    private OrderRepository orderRepository;

    @RabbitListener(queues = RabbitMQConfiguration.DEAD_LETTER_QUEUE_NAME)
    public void consumeChargingDeadLetterQueue(OrderMQPO orderMQPO, Message message, Channel channel) {
            log.info("RabbitMQOrderConsumer 消费死信队列");

            orderRepository.updateOrderStatusFromProcess2ExceptionEnd(orderMQPO.getOrderNo());
    }

}
