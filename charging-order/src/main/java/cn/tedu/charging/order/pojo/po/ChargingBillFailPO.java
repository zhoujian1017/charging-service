package cn.tedu.charging.order.pojo.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * 订单失败表 PO  persist 持久的
 * ChargingBillFailPO 里的属性要和表里的字段一一对应
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@TableName("charging_bill_fail") //指定表名 和数据库中具体的表进行映射
public class ChargingBillFailPO {

    @TableId(type = IdType.AUTO)
    Integer id;

    /**
     * 订单信息 订单编号
     */
    String billId;


    /**
     * 设备信息 枪id 如果是电商 这里应该是商品id
     */
    Integer gunId;

    /**
     * 用户信息  用户id
     */
    Integer userId;

    /**
     * 失败原因
     */
    String failDesc;

}
