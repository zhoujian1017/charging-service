package cn.tedu.charging.order.dao.mapper;

import cn.tedu.charging.order.pojo.po.ChargingBillSuccessPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderSuccessMapper extends BaseMapper<ChargingBillSuccessPO> {
}
