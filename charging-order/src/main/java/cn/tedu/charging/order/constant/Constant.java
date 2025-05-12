package cn.tedu.charging.order.constant;

public class Constant {

    /**
     * 表示枪被使用
     */
    public static final Integer GUN_STATS_BUSY = 2;

    /**
     * 表示枪未使用
     */
    public static final Integer GUN_STATS_FREE = 1;

    /**
     * 表示枪故障
     */
    public static final Integer GUN_STATS_ERROR = 3;


    /**
     * 开始充电指令，订单服务给设备发送
     */
    public static final String START_CHARGING = "start_charging";

    /**
     * 停止充电指令，订单服务给设备发送
     */
    public static final String STOP_CHARGING = "stop_charging";

    /**
     * 设备给订单服务发送的 开始充电的结果 成功
     */
    public static final String RESULT_START_CHARGING_SUCCESS = "start_success";

    /**
     * 设备给订单服务发送的 开始充电的结果 失败
     */
    public static final String RESULT_START_CHARGING_FAIL = "start_fail";

}
