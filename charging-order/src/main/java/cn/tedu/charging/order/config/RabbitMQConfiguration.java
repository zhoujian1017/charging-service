package cn.tedu.charging.order.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * rabbit-mq 配置类
 * 配置 连接信息,用户名,密码
 * 配置 队列 交换机 并且还能进行队列和交换机绑定
 */
@Configuration
public class RabbitMQConfiguration {


    public static final String EXCHANGE_NAME = "charging_order_exchange";

    private static final String QUEUE_NAME = "charging_order_queue";

    public static final String ROUTING_KEY = "charging_order_routing_key";

    private static final String DEAD_LETTER_EXCHANGE_NAME = "charging_dead_letter_order_exchange";

    public static final String DEAD_LETTER_QUEUE_NAME = "charging_dead_order_queue";

    private static final String DEAD_LETTER_ROUTING_KEY = "charging_dead_order_routing_key";

    /**
     * 定义正常的订单的Exchange
     * @return
     */
    @Bean
    public DirectExchange orderExchange(){
        //消息持久化,不能丢
        Boolean durable = true;
        return new DirectExchange(EXCHANGE_NAME,durable,false);
    }

    /**
     * 定义正常的订单的Queue
     * @return
     */
    @Bean
    public Queue orderQueue(){
        Map<String,Object> args = new HashMap<>();
        //设置消息的存活时间
        //模拟充电充满需要 2分钟
        Integer ttl = 2 * 60 * 1000;
        args.put("x-message-ttl",ttl);
        //设置queue的死信exchange
        args.put("x-dead-letter-exchange",DEAD_LETTER_EXCHANGE_NAME);
        //设置queue和死信exchange绑定的 routingKey
        args.put("x-dead-letter-routing-key",DEAD_LETTER_ROUTING_KEY);
        //惰性队列,在消息很多的时候,把消息存储到磁盘,避免消息积压,占用内存
        args.put("x-queue-mode","lazy");
        //消息持久化,不能丢
        Boolean durable = true;
        return new Queue(QUEUE_NAME,durable,true,false,args);
    }

    /**
     * 把订单队列绑定到订单交换机
     * @return
     */
    @Bean
    public Binding orderBinding(){
        return BindingBuilder.bind(orderQueue())
                .to(orderExchange()).with(ROUTING_KEY);
    }

    /**
     * 定义死信的交换机
     * @return
     */
    @Bean
    public DirectExchange deadLetterExchange(){
        //消息持久化,不能丢
        Boolean durable = true;
        return new DirectExchange(DEAD_LETTER_EXCHANGE_NAME,durable,false);
    }

    /**
     * 定义死信的队列
     * @return
     */
    @Bean
    public Queue deadLetterQueue(){
        //消息持久化,不能丢
        Boolean durable = true;
        return new Queue(DEAD_LETTER_QUEUE_NAME,durable);
    }


    /**
     * 把死信队列绑定到死信交换机
     * @return
     */
    @Bean
    public Binding deadLetterOrderBinding(){
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange()).with(DEAD_LETTER_ROUTING_KEY);
    }

}

