package cn.tedu.charging.order.pojo.po;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * 订单服务发送给RabbitMQ的数据
 *
 * 需要实现序列化
 * implements Serializable
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderMQPO implements Serializable {
    /**
     * 订单编号
     */
    String orderNo;

}
