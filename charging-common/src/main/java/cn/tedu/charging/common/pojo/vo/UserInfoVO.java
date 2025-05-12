package cn.tedu.charging.common.pojo.vo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInfoVO {

    /**
     * 车辆ID 假定用户只允许一个车辆
     */
    Integer carId;
}
