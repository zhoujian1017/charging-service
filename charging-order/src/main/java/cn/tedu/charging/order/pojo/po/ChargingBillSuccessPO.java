package cn.tedu.charging.order.pojo.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * 订单成功表 PO  persist 持久的
 * ChargingBillSuccessPO 里的属性要和表里的字段一一对应
 *
 * 一个订单  用户在平台需要什么服务,订单表记录了用户和平台就某项服务的一个合同,凭据 证明
 *  用户信息 (用户id,用户地址....)
 *  订单信息 (订单编号,订单状态,订单创建时间...)
 *      订单状态 充电中,充电完成 (正常完成,异常完成)
 *      电商订单 新建(订单)-->未支付(支付)-->支付成功(支付)-->
 *              待发货(物流)--已发货(物流)-->已收货(用户)
 *              按照业务 拆分
 *              订单状态   新建 完成
 *              支付状态  未支付 支付成功
 *              物流状态  待发货  已发货
 *
 *  服务信息 (设备信息,商品信息...)
 *  履约信息 可选 (什么方式,什么时间 完成服务,电商里的 快递信息)
 *  支付信息 ()
 *
 *
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
//charing_bill_success_p_o
@TableName("charging_bill_success") //指定表名 和数据库中具体的表进行映射
public class ChargingBillSuccessPO {


    @TableId(type = IdType.AUTO)
    Integer id;

    /**
     * 订单信息 订单编号
     */
    String billId;

    /**
     * 订单信息 订单状态
     * 订单状态的流转
     */
    Integer billStatus;

    /**
     * 设备信息 枪id 如果是电商 这里应该是商品id
     */
    Integer gunId;

    /**
     * 用户信息  用户id
     */
    Integer userId;

}
