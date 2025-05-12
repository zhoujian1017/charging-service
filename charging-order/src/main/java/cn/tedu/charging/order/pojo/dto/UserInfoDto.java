package cn.tedu.charging.order.pojo.dto;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * 用户服务和订单服务传输的数据对象
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInfoDto {

    Integer carId;
}
