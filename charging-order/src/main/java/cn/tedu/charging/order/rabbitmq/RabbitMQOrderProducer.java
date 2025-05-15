package cn.tedu.charging.order.rabbitmq;

import cn.tedu.charging.order.config.RabbitMQConfiguration;
import cn.tedu.charging.order.pojo.po.OrderMQPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMQOrderProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendOrder(OrderMQPO orderMQPO) {

        //设置RabbitMQ return的回调类,回执
        rabbitTemplate.setMandatory(true);
        //消息是发送Exchange , Exchange收到后,转发到Queue,当queue不存在,转发失败
        rabbitTemplate.setReturnsCallback(returned -> {
            //returned 包含被退掉信息 包括 消息本身,错误码,错误信息,交换机,路由key
            log.debug("returnedMessage:{}",returned);
        });


        //设置RabbitMQ 确认的回调类 保证不丢要在生产者开启confirm机制
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            log.debug("Rabbit确认回调:{}",ack);
            if (ack) {
                log.debug("RabbitMQ成功收到消息");
            }else {
                log.debug("RabbitMQ没有成功收到消息,打日志,进行重试,重新发送");
            }
        });
        rabbitTemplate.convertAndSend(RabbitMQConfiguration.EXCHANGE_NAME,
                RabbitMQConfiguration.ROUTING_KEY, orderMQPO);
    }
}
