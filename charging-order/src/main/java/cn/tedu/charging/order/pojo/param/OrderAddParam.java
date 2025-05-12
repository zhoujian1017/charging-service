package cn.tedu.charging.order.pojo.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * 创建订单的入参
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderAddParam {

    @ApiModelProperty(value = "用户id")
    Integer userId;

    @ApiModelProperty(value = "充电桩编号")
    Integer pileId;

    @ApiModelProperty(value = "充电枪编号")
    Integer gunId;


    /**
     * 创建时间
     * 不应该由页面来传,服务端生成服务器的时间
     */
    //Long createTime;

}
