package cn.tedu.charging.order.pojo.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * 充电桩给订单服务发送的消息
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChargingResultDto {

    /**
     * 订单号
     */
    String orderNo;

    /**
     * 返回结果
     */
    String result;
}