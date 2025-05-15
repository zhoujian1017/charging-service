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
    public void consumeChargingDeadLetterQueue(OrderMQPO orderMQPO, Message message, Channel channel) throws Exception{
            log.info("RabbitMQOrderConsumer 消费死信队列");
//把状态是 正在充电中的 成功订单数据 的状态 修改 为 异常结束
        // update charging_bill_success
        // set bill_status = 3 (异常结束)
        // where bill_id = orderNo and bill_status = 1 (充电中)
        // 2 to
        orderRepository.updateOrderStatusFromProcess2ExceptionEnd(orderMQPO.getOrderNo());
        //1 用了rabbitmq了么? 用了
        //2 怎么用的,解决了什么业务问题 ? 通过死信队列实现延迟消息,处理超时订单 因为设备故障导致订单状态一直是充电中订单数据
        //3你的消息能丢失么? 不能
        //4 如何保证消息不丢失?
        //  生产者(confirm)  ---> 消息队列(RabbitMQ)(持久化,集群) ---> 消费者(手动ack)
        //      1 生产者不能丢 ,要保证消息成功发送到消息队列, 消息队列告诉生产者 消息我已经收到了 confirm 机制
        //      2 消息队列不能丢 durable  把queue,和exchange durable设置为true 开启持久化
        //      3 消费者不能丢  关闭自动ack ,手动ack
        //5  这样就不会丢了么? 会  因为消息队列如果部署的是单机,单点故障,,持久化是指消息持久化到磁盘,磁盘坏了消息恢复不了,消息丢失
        //6 rabbitmq 集群是怎么实现的?





        //消息队列中的ack是什么意思?
        //consumeChargingDeadLetterQueue 消费者 从RabbitMQ 拿到消息 处理业务 更新订单状态
        //  正常情况
        //      更新订单状态成功,返回ack给消息队列
        //      消息队列收到消费者发送的ack后,认为消费者消费消息成功,会把这条消息从消息队列中删除,消费者可以继续消费下一条消息
        //  异常情况
        //      更新订单状态,数据库出现异常,更新失败,不返回ack给消息队列
        //      消息队列没有收到消费者给的ack,认为消费者消费消息失败,不会删除这条消息,会一直保存
        //      等数据库恢复了,继续重新消费这条消息,消息没丢,继续更新订单的状态


        //手动ack
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        channel.basicAck(deliveryTag,true);
    }

}
