package cn.tedu.charging.order.dao.repository.impl;

import cn.tedu.charging.order.constant.OrderStatusConstant;
import cn.tedu.charging.order.dao.mapper.OrderFailMapper;
import cn.tedu.charging.order.dao.mapper.OrderSuccessMapper;
import cn.tedu.charging.order.dao.repository.OrderRepository;
import cn.tedu.charging.order.pojo.po.ChargingBillFailPO;
import cn.tedu.charging.order.pojo.po.ChargingBillSuccessPO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("orderRepository")
public class OrderRepositoryImpl implements OrderRepository {

    @Autowired
    private OrderSuccessMapper orderSuccessMapper;

    @Autowired
    private OrderFailMapper orderFailMapper;

    @Override
    public void updateOrderStatusFromProcess2ExceptionEnd(String orderNo) {
        //更新的目标值 bill_status = 3 (异常结束)
        ChargingBillSuccessPO chargingBillSuccessPO = new ChargingBillSuccessPO();
        chargingBillSuccessPO.setBillStatus(OrderStatusConstant.ORDER_STATUS_EXCEPTION_END);

        //UpdateWrapper 更新的条件 where  bill_id = orderNo and bill_status = 1
        UpdateWrapper<ChargingBillSuccessPO> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("bill_status", OrderStatusConstant.ORDER_STATUS_PROGRESS);
        updateWrapper.eq("bill_id",orderNo);
        orderSuccessMapper.update(chargingBillSuccessPO,updateWrapper);
    }

    @Override
    public ChargingBillSuccessPO getSuccessOrder(String orderNo) {
        QueryWrapper<ChargingBillSuccessPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bill_id",orderNo);
        //queryWrapper 查询的条件
        // where bill_id = orderNo
        return orderSuccessMapper.selectOne(queryWrapper);
    }

    @Override
    public ChargingBillFailPO getFailOrder(String orderNo) {
        QueryWrapper<ChargingBillFailPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bill_id",orderNo);
        return orderFailMapper.selectOne(queryWrapper);
    }

    @Override
    public void saveFailOrder(ChargingBillFailPO chargingBillFailPO) {
        orderFailMapper.insert(chargingBillFailPO);
    }

    @Override
    public void updateDeviceInfo(String orderNo, Integer gunId) {
        //更新的目标值 gunId = gunId
        ChargingBillSuccessPO chargingBillSuccessPO = new ChargingBillSuccessPO();
        chargingBillSuccessPO.setGunId(gunId);
        //UpdateWrapper 更新的条件 where  bill_id = orderNo
        UpdateWrapper<ChargingBillSuccessPO> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("bill_id",orderNo);
        orderSuccessMapper.update(chargingBillSuccessPO,updateWrapper);
    }
}
