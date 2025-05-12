package cn.tedu.charging.order.service.impl;

import cn.tedu.charging.common.pojo.JsonResult;
import cn.tedu.charging.common.pojo.param.GunStatusUpdateParam;
import cn.tedu.charging.common.pojo.vo.StationInfoVO;
import cn.tedu.charging.common.pojo.vo.UserInfoVO;
import cn.tedu.charging.common.utils.JsonUtils;
import cn.tedu.charging.order.constant.Constant;
import cn.tedu.charging.order.constant.MqttConstant;
import cn.tedu.charging.order.fegin.DeviceClient;
import cn.tedu.charging.order.fegin.UserClient;
import cn.tedu.charging.order.mqtt.MqttProducer;
import cn.tedu.charging.order.pojo.dto.ChargingDto;
import cn.tedu.charging.order.pojo.dto.StationInfoDto;
import cn.tedu.charging.order.pojo.dto.UserInfoDto;
import cn.tedu.charging.order.pojo.param.OrderAddParam;
import cn.tedu.charging.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    /**
     * 创建订单
     * @param orderAddParam
     * @return
     */
    @Override
    public String createOrder(OrderAddParam orderAddParam) {
        //创建订单的流程 自顶向下编程
        // 高内聚 低耦合 单一职责 解耦(你依赖我,我变了,你也得跟着变,我们耦合在一起)
        //1 创建订单编号
        log.debug("开始生成订单号");
        String orderNo = getOrderNo();
        log.debug("生成订单号:{}",orderNo);
        //2 获取运营商和场站信息
        log.debug("获取运营商和场站信息入参-枪id:{}",orderAddParam.getGunId());
        StationInfoDto stationInfoDto = getStationInfoByGunId(orderAddParam.getGunId());
        log.debug("获取运营商和场站信息出参-枪id:{},出参:{}",orderAddParam.getGunId(),stationInfoDto);
        //3 获取用户车辆信息
        log.debug("获取用户车辆信息入参-用户id   :{}",orderAddParam.getUserId());
        UserInfoDto userInfoDto = getUserInfoByUserId(orderAddParam.getUserId());
        log.debug("获取用户车辆信息出参-用户id:{},出参:{}",orderAddParam.getUserId(),userInfoDto);
        //4 修改充电枪的状态 把充电桩状态 变为 已使用
        //Boolean success = updateGunStatus(orderAddParam.getGunId(),2);
        log.debug("修改充电枪的状态入参-枪id:{}",orderAddParam.getGunId());
        Boolean success = updateGunStatusBusy(orderAddParam.getGunId());
        log.debug("修改充电枪的状态出参-枪id:{},出参:{}",orderAddParam.getGunId(),success);

        //5 检查用户余额 ? todo
        //  5.1 要 更安全 提前检查,但是有可能,充电桩故障,根本充不了电,代码逻辑多跑了一次
        //  5.2 不要 什么时候检查呢? 应该充电桩正常,能充电的时候,每次充电桩同步充电进度的时候检查 必须要做
        //  总结 用户余额检查 创建订单的时候可以检查, 每次同步充电进度的时候必须检查


        //6 给充电桩发送开始充电指令
        log.debug("给充电桩发送开始充电指令入参-orderNo:{},桩id:{},枪id:{}",orderNo,orderAddParam.getPileId(),orderAddParam.getGunId());
        startCharging(orderNo,orderAddParam.getPileId(),orderAddParam.getGunId());
        log.debug("成功给充电桩发送开始充电指令入参-orderNo:{},桩id:{},枪id:{}",orderNo,orderAddParam.getPileId(),orderAddParam.getGunId());
        return orderNo;
    }

    @Autowired
    private MqttProducer mqttProducer;
    /**
     * 给充电桩发送开始充电指令
     * @param orderNo
     * @param pileId
     * @param gunId
     */
    private void startCharging(String orderNo, Integer pileId, Integer gunId) {
        //topic 定义为 前缀 + 桩id  不加枪id,设备定义消息的时候,接收到这个桩上的所有消息
        //然后解析json获取枪id,开始让指定的枪充电
        String topic = MqttConstant.TOPIC_START_CHARGING_PREFIX +pileId;
        ChargingDto chargingDto = new ChargingDto();
        chargingDto.setOrderNo(orderNo);
        chargingDto.setPileId(pileId);
        chargingDto.setGunId(gunId);
        chargingDto.setMsg(Constant.START_CHARGING);
        String json = JsonUtils.toJson(chargingDto);
        log.debug("订单服务发送充电指令到设备topic:{},消息:{}",topic,chargingDto);
        mqttProducer.send(topic,json);
        log.debug("订单服务发送充电指令到设备成功.topic:{},消息:{}",topic,chargingDto);
    }


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserClient userClient;

    /**
     * 调用用户服务获取用户车辆信息
     * @param userId
     * @return
     */
    private UserInfoDto getUserInfoByUserId(Integer userId){
        return getUserInfoByUserIdByFeign(userId);
    }


    /**
     * 通过feign 调用用户服务
     * @param userId
     * @return
     */
    private UserInfoDto getUserInfoByUserIdByFeign(Integer userId){
        JsonResult<UserInfoVO> userCarInfo = userClient.getUserCarInfo(userId);
        if (userCarInfo != null) {
            UserInfoVO userInfoVO = userCarInfo.getData();
            if (userInfoVO != null) {
                UserInfoDto userInfoDto = new UserInfoDto();
                userInfoDto.setCarId(userInfoVO.getCarId());
                return userInfoDto;
            }
        }
        return null;
    }

    /**
     * 获取用户信息 绑定的车辆信息
     * 调用用户服务的一个接口 来获取用户绑定的车辆信息
     * @param userId
     * @return
     */
    private UserInfoDto getUserInfoByUserIdByRestTemplate(Integer userId) {
        ParameterizedTypeReference<JsonResult<UserInfoVO>> resultParameterizedTypeReference =
                new ParameterizedTypeReference<JsonResult<UserInfoVO>>() {};
        String url = chooseUrl();
        log.debug("通过随机算法获取用户服务地址:{}",url);
        log.debug("通过RestTemplate调用用户服务获取用户信息,地址:{},入参:userId:{}",url,userId);
        ResponseEntity<JsonResult<UserInfoVO>> entity =
                restTemplate.exchange(url, HttpMethod.GET,
                        null,
                        resultParameterizedTypeReference,
                        userId);
        log.debug("通过RestTemplate调用用户服务获取用户信息,地址:{},入参:userId:{},出参:{}",url,userId,entity);
        if (entity != null) {
            HttpStatus statusCode = entity.getStatusCode();
            if (HttpStatus.OK == statusCode) {
                JsonResult<UserInfoVO> body = entity.getBody();
                UserInfoVO userInfoVO = body.getData();
                UserInfoDto userInfoDto = new UserInfoDto();
                userInfoDto.setCarId(userInfoVO.getCarId());
                return userInfoDto;
            }
        }
        return null;
    }

    /**
     * 通过随机算法获取用户服务的地址
     * @return
     */
    private String chooseUrl(){
        //硬编码
        String url = "http://localhost:8080/user/info/{1}";
        String url1 = "http://localhost:8081/user/info/{1}";
        String url2 = "http://localhost:8082/user/info/{1}";
        String[] services = {url, url1, url2};
        Random random = new Random();
        int r = random.nextInt(services.length);
        return services[r];
    }

    @Autowired
    private DeviceClient deviceClient;

    /**
     * 获取场站信息和运营商信息
     * @param gunId
     * @return
     */
    private StationInfoDto getStationInfoByGunId(Integer gunId) {
        JsonResult<StationInfoVO> stationInfo = deviceClient.getStationInfo(gunId);
        if (stationInfo != null) {
            StationInfoVO data = stationInfo.getData();
            System.out.println(data+"111111");
            if (data != null) {
                StationInfoDto stationInfoDto = new StationInfoDto();
                //stationInfoDto.setStationId(data.getStationId());
                //stationInfoDto.setOperatorId(data.getOperatorId());
                BeanUtils.copyProperties(data,stationInfoDto);
                return stationInfoDto;
            }
        }
        return null;
    }

    /**
     * 把充电枪的状态修改为 已经使用
     * @param gunId
     * @return
     */
    private Boolean updateGunStatusBusy(Integer gunId) {
        GunStatusUpdateParam param = new GunStatusUpdateParam();
        param.setGunId(gunId);
        //param.setStatus(2);//魔数 代码里不要有魔数,应该用常量或者枚举
        param.setStatus(Constant.GUN_STATS_BUSY);
        JsonResult<Boolean> jsonResult = deviceClient.updateGunStatus(param);
        if (jsonResult != null) {
            return jsonResult.getData();
        }
        return false;
    }

    /**
     * 获取订单号  唯一 为了保证唯一 需要考虑在分布式场景下
     * 唯一 随机的么?
     * 1 能不能用订单表的主键? 主键是自增 单表就是唯一  分库分表 自增不能保证唯一 会重复
     * 随机 UUID 不重复
     * UUID能当主键? 不能 为什么? 和mysql数据库主键的索引底层实现有关系? todo
     * 要不要递增
     * @return
     *
     * 自己生成订单号
     * 分布式id生成器 分布式场景下生成唯一的id
     * 美团 leaf https://www.cnblogs.com/strongmore/p/17205162.html
     * 雪花算法 https://www.jianshu.com/p/1af94260664a
     *
     * 固定开始 10000 + 随机数 +  时间戳
     *
     */
    private String getOrderNo(){
        String start = "10000";
        long now = System.currentTimeMillis();
        Random random = new Random();
        int r = random.nextInt(100);
        String orderNo = start + "_" + r + "_" + now;
        return orderNo;
    }
}
