package cn.tedu.charging.order.mqtt;

import cn.tedu.charging.common.utils.JsonUtils;
import cn.tedu.charging.order.constant.Constant;
import cn.tedu.charging.order.constant.MqttConstant;
import cn.tedu.charging.order.constant.OrderStatusConstant;
import cn.tedu.charging.order.pojo.dto.ChargingResultDto;
import cn.tedu.charging.order.pojo.po.ChargingBillFailPO;
import cn.tedu.charging.order.pojo.po.ChargingBillSuccessPO;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;

@Slf4j
public class MqttConsumer implements MqttCallbackExtended {
    private MqttClient mqttClient;
    private MqttContext mqttContext;

    public MqttConsumer(MqttContext mqttContext,MqttClient mqttClient){
        this.mqttContext = mqttContext;
        this.mqttClient = mqttClient;
    }

    /**
     * 连接完成,表示订单服务和EMQX消息中间件连接成功
     * @param reconnect If true, the connection was the result of automatic reconnect.
     * @param serverURI The server URI that the connection was made to.
     */
    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        log.debug("MqttConsumer#connectComplete方法被调用了,表示连接成功");
        try {
            log.debug("连接成功,订阅topic:{},用来接收设备发送的充电结果信息", MqttConstant.TOPIC_CHARGING_RESULT);
            mqttClient.subscribe(MqttConstant.TOPIC_CHARGING_RESULT);
            log.debug("连接成功,订阅topic:{} 成功,用来接收设备发送的充电结果信息",MqttConstant.TOPIC_CHARGING_RESULT);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 连接丢失
     * @param cause the reason behind the loss of connection.
     */
    @Override
    public void connectionLost(Throwable cause) {
        log.debug("MqttConsumer#connectionLost方法被调用了,表示连接丢失");
    }

    /**
     * 消息到达,不是订单服务的消息到达设备
     * 设备发送消息,到达了订单服务
     * @param topic name of the topic on the message was published to
     * @param message the actual message.
     * @throws Exception
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.debug("MqttConsumer#messageArrived方法被调用了,消息到达,topic:{},message:{}",topic,message);
        ChargingResultDto chargingResultDto = null;
        try{
            //把设备发送的消息JSON转换java对象ChargingResultDto
            chargingResultDto =
                    JsonUtils.fromJson(message.toString(), ChargingResultDto.class);
            log.debug("消息转换为java对象:{}",chargingResultDto);
        }catch (Exception e){
            log.error("设备发送的开始充电结果消息格式{}有问题,请检查!",message);
        }
        //判断java对象不能为空 chargingResultDto
        if (chargingResultDto != null) {
            //获取设备充电结果
            String result = chargingResultDto.getResult();
            //判断设备充电结果
            if (Constant.RESULT_START_CHARGING_SUCCESS.equals(result)) {
                //设备开始充电成功
                log.debug("充电桩开始充电成功");
                ChargingBillSuccessPO chargingBillSuccessPO = new ChargingBillSuccessPO();
                chargingBillSuccessPO.setBillId(chargingResultDto.getOrderNo());
                chargingBillSuccessPO.setBillStatus(OrderStatusConstant.ORDER_STATUS_PROGRESS);

                log.debug("保存成功订单记录:{}",chargingBillSuccessPO);

                //如果设备重复发送消息
                //会导致 成功订单表 charging_bill_success 会重复的保存订单数据
                //如何解决
                //1 给订单表 订单编号 创建一个唯一索引,唯一索引的意思 这个表里不能有重复的订单编号
                // 加完唯一索引后,如果有重复的记录要保存 数据库会抛出异常
                // Duplicate entry '10000_58_1721720859912' for key 'charging_bill_success_bill_id_unique_key'
                // 从而避免订单成功表有重复的订单记录
                // 异常可以处理,可以不处理 通常接入监控系统  监控系统
                // 如果固定的时间之内 重复发送消息,设备出问题了,通知调用方 设备维修人员
                //2 先查询订单成功表是否有数据,如果没有 插入,如果有 跳过不保存,
                // 代码怎么写? 原子性的问题  todo
                //3 记录状态 如果处理过了,不再重复处理  redis的布隆过滤器 自己了解 todo
                try{
                    Integer row = mqttContext.getOrderSuccessMapper().insert(chargingBillSuccessPO);
                    log.debug("保存成功订单记录:{},完成,影响行数:{}",chargingBillSuccessPO,row);
                }catch (Exception e) {
                    //接入监控系统
                    log.error("保存成功订单数据失败",e);
                }
            }else {
                //不是成功,都是失败
                log.debug("充电桩开始充电失败");
                ChargingBillFailPO chargingBillFailPO = new ChargingBillFailPO();
                chargingBillFailPO.setBillId(chargingResultDto.getOrderNo());
                chargingBillFailPO.setFailDesc("充电桩故障-无法正常充电");
                log.debug("保存失败订单记录:{}",chargingBillFailPO);
                Integer row = mqttContext.getOrderFailMapper().insert(chargingBillFailPO);
                log.debug("保存失败订单记录:{},完成,影响行数:{}",chargingBillFailPO,row);
            }
        }
    }

    /**
     * 消息处理完成
     * @param token the delivery token associated with the message.
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.debug("MqttConsumer#deliveryComplete方法被调用了,表示消息处理完成");
    }
}
