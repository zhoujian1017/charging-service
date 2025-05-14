package cn.tedu.charging.order.mqtt;

import cn.tedu.charging.order.dao.mapper.OrderFailMapper;
import cn.tedu.charging.order.dao.mapper.OrderSuccessMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
public class MqttContext {

    @Autowired
    private OrderSuccessMapper orderSuccessMapper;

    @Autowired
    private OrderFailMapper orderFailMapper;


}
