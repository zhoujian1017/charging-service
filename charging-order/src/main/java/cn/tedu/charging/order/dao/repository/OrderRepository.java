package cn.tedu.charging.order.dao.repository;

import cn.tedu.charging.order.pojo.po.ChargingBillFailPO;
import cn.tedu.charging.order.pojo.po.ChargingBillSuccessPO;

public interface OrderRepository {

    /**
     * 更新 订单状态 充电中  为 异常结束
     * @param orderNo
     */
    void updateOrderStatusFromProcess2ExceptionEnd(String orderNo);

    ChargingBillSuccessPO getSuccessOrder(String orderNo);

    ChargingBillFailPO getFailOrder(String orderNo);

    void saveFailOrder(ChargingBillFailPO chargingBillFailPO1);

    void updateDeviceInfo(String orderNo, Integer gunId);
}
