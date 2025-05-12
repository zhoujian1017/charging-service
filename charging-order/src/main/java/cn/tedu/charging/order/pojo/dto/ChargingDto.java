package cn.tedu.charging.order.pojo.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * 给设备发送的数据
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ChargingDto {
    /**
     * 订单号
     */
    String orderNo;

    /**
     * 桩id
     */
    Integer pileId;


    /**
     * 枪id
     */
    Integer gunId;

    /**
     * 指令
     * 1 开始充电
     * 2 结束充电
     */
    String msg;
}
