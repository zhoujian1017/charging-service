package cn.tedu.charging.order.constant;

public class MqttConstant {

    /**
     * 开始充电topic 前缀
     */
    public static final String TOPIC_START_CHARGING_PREFIX = "/topic/start/";

    /**
     * 设备和订单同步数据的topic 订单用来接收设备发送的 充电结果 两种结果 1 开始成功 2 开始失败
     */
    public static final String TOPIC_CHARGING_RESULT = "/topic/charging/result";

    /**
     * 设备同步topic 同步充电状态
     */
    public static final String TOPIC_CHARGING_PROCESS = "/topic/charging/process";

}
