package cn.tedu.charging.common.pojo.vo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * 场站信息
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StationInfoVO {

    /**
     * 运营商ID
     */
    Integer operatorId;

    /**
     * 场站ID
     */
    Integer stationId;
}
