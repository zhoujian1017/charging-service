package cn.tedu.charging.order.pojo.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * 设备服务和订单服务传输的数据对象
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StationInfoDto {

    /**
     * 场站id
     */
    Integer stationId;

    /**
     * 运营商id
     */
    Integer operatorId;

}
