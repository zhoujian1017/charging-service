package cn.tedu.charging.order.job;

import cn.tedu.charging.common.pojo.param.GunStatusUpdateParam;
import cn.tedu.charging.order.constant.Constant;
import cn.tedu.charging.order.dao.repository.OrderRepository;
import cn.tedu.charging.order.fegin.DeviceClient;
import cn.tedu.charging.order.pojo.po.ChargingBillFailPO;
import cn.tedu.charging.order.pojo.po.ChargingBillSuccessPO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;
import java.util.UUID;


/**
 * 设备自检延迟处理任务
 * 目标 是尽快发现设备故障
 * 如果没有自检任务,设备出现故障,走死信队列,时间会很长
 * 加定时任务 任务的执行时间 是 当前时间 + 1分钟(设备自检时间)
 * 1分钟就能发现设备故障, 快速通知用户 通知维修人员
 * 设备开始就没有响应 开始就出故障
 *
 * 死信队列是检查最后应该完成,而没有完成 把充电中的订单 修改为异常结束,如果完成,无需处理
 * 设备开始有响应 中间出故障
 *
 */
@Slf4j
public class DeviceCheckJob implements Job {

    /**
     * 必须需要无参的构造器
     * 如果没有 在初始化的时候 newInstance ,获取构造器 getConstructor0,获取失败
     * NoSuchMethodException 找不到方法异常 (找不到默认的构造器)
     */
    public DeviceCheckJob() {}

    /**
     * 添加设备自检定时任务
     * @param orderNo 订单号
     * @param gunId 枪id
     * @throws Exception
     */
    public DeviceCheckJob(String orderNo,Integer gunId) throws Exception {
        log.debug("添加设备自检定时任务:订单号-{},枪id-{}",orderNo,gunId);
        //创建调度器工厂
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        //通过调度器工厂获取调度器
        Scheduler scheduler = schedulerFactory.getScheduler();

        //创建定时任务
        JobDetail jobDetail = JobBuilder.newJob(DeviceCheckJob.class)
                //定义定时任务的名称和组
                .withIdentity("DeviceCheckJob" + UUID.randomUUID(),"device_check_job")
                //传入订单号
                .usingJobData("orderNo",orderNo)
                //插入枪id
                .usingJobData("gunId",gunId)
                .build();

        //设备自检需要1分钟 模拟
        long deviceCheckTime = 1 * 60 * 1000;
        //当前时间
        long now = System.currentTimeMillis();
        //任务触发的时间
        Date triggerTime = new Date(now + deviceCheckTime);
        //创建触发器 并设置时间
        Trigger trigger = TriggerBuilder.newTrigger().startAt(triggerTime).build();

        //调度器 把 任务 和 触发器  绑定
        scheduler.scheduleJob(jobDetail,trigger);
        //启动调度器
        scheduler.start();
    }

    /**
     * 执行 干一些事情
     * @param context
     * @throws JobExecutionException
     */
    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
            log.debug("执行设备自检任务:{}",context);
            JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
            //获取订单号
            String orderNo = jobDataMap.getString("orderNo");
            //获取枪id
            Integer gunId = jobDataMap.getInt("gunId");
            //7. 判断是否有订单记录
            //8. 如果有成功订单记录 不用管 无需处理 无需处理 设备有响应,设备给订单服务发送了开始充电成功指令
            //9. 如果没有成功订单记录
            //10. 判断是否有失败订单记录   设备有响应,设备给订单服务发送了开始充电失败指令
            //11. 如果有失败订单记录  故障原因 设备故障-无法充电, 不用管,无需处理
            //12. 如果没有失败订单记录 插入失败订单记录  故障原因 设备无响应

            //DeviceCheckJob 没有在 Spring-IOC 容器里, 可以通过 applicationContext  从容器获取 OrderRepository 实例
        OrderRepository orderRepository =
                SpringContextJobUtils.getBean("orderRepository", OrderRepository.class);
        DeviceClient deviceClient = SpringContextJobUtils.getBean(DeviceClient.class);
        //获取成功订单记录
        ChargingBillSuccessPO chargingBillSuccessPO = orderRepository.getSuccessOrder(orderNo);
        //7. 判断是否有订单记录
        if (chargingBillSuccessPO == null) {
            //9. 如果没有成功订单记录
            ChargingBillFailPO chargingBillFailPO = orderRepository.getFailOrder(orderNo);
            //10. 判断是否有失败订单记录
            if (chargingBillFailPO == null) {
                //12. 如果没有失败订单记录 插入失败订单记录  故障原因 设备无响应
                ChargingBillFailPO chargingBillFailPO1 = new ChargingBillFailPO();
                chargingBillFailPO1.setFailDesc("设备自检失败-设备无响应");
                chargingBillFailPO1.setBillId(orderNo);
                log.debug("设备自检失败,保存失败订单记录:{}",chargingBillFailPO1);
                orderRepository.saveFailOrder(chargingBillFailPO1);
                log.debug("设备自检失败,保存失败订单记录:{} 成功.",chargingBillFailPO1);


                //修改设备状态为 故障  通知为维修人员
                log.debug("修改设备状态为故障:枪id:{},状态:{}",gunId, Constant.GUN_STATS_ERROR);
                GunStatusUpdateParam updateParam = new GunStatusUpdateParam();
                updateParam.setStatus(Constant.GUN_STATS_ERROR);
                updateParam.setGunId(gunId);

                deviceClient.updateGunStatus(updateParam);
                log.debug("修改设备状态为故障:枪id:{},状态:{} 成功",gunId,Constant.GUN_STATS_ERROR);

                //通知设备维修人员
                log.debug("调用 保障服务 成功,通知维修人员对设备进行检查和维护",gunId,Constant.GUN_STATS_ERROR);
                // 1 创建保障微服务
                // 2 创建一个接口,创建维修单
                // 3 订单服务增加 保障服务 feignClient
                // 4 调用feignClient 创建维修单
                // 和保障服务通信
                //   1 通过open-feign直接调用
                //   2 通过rabbit-mq  订单服务是消息的发送者,保障服务是消息的消费者,保障服务消费到消息后
                //   创建维修单,通知维修人员,维修人员接收到消息,进行设备检查和维修 (需要考虑幂等)

                //通知充电用户
                log.debug("通知用户设备故障,请您更换充电设备,给您带来的不便,敬请谅解..",gunId,Constant.GUN_STATS_ERROR);
                log.debug("调用 营销系统(优惠券服务)给用户发送优惠券 1张价值100的优惠已经发到你的账户请注意查收...");
                // 1 创建优惠券微服务
                // 2 创建一个接口,发送优惠券
                // 3 订单服务增加 优惠券微服务 feignClient
                // 4 调用feignClient 发送优惠券
                // 和 优惠券通信
                //   1 通过open-feign直接调用
                //   2 通过rabbit-mq  订单服务是消息的发送者,优惠券服务是消息的消费者,优惠券服务消费到消息后
                //   给用户发送优惠券(需要考虑幂等)
            }else {
                log.debug("设备自检失败-设备有响应,但是充不了电,这里逻辑无需修改 设备失败原因");
            }
        }else {
            log.debug("有成功订单记录,设备自检成功,有响应,能充电");
            log.debug("更新设备信息到订单表,枪id:{},订单编号:{},这里不是必须的操作,作为演示",gunId,orderNo);
            orderRepository.updateDeviceInfo(orderNo,gunId);
            log.debug("更新设备信息到订单表成功,枪id:{},订单编号:{},这里不是必须的操作,作为演示");
        }
    }
}
