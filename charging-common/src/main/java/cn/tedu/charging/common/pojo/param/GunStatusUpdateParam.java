package cn.tedu.charging.common.pojo.param;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * 更新充电枪状态入参
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GunStatusUpdateParam {

    /**
     * 枪ID
     */
    Integer gunId;
    /**
     * 枪状态
     */
    Integer status;
}
