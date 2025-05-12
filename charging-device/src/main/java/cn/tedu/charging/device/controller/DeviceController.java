package cn.tedu.charging.device.controller;

import cn.tedu.charging.common.pojo.JsonResult;
import cn.tedu.charging.common.pojo.vo.StationInfoVO;
import cn.tedu.charging.common.pojo.param.GunStatusUpdateParam;
import cn.tedu.charging.device.service.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/*
* 订单控制器
* */


@RestController
@RequestMapping("/device")
@Slf4j
@Api(tags = "设备接口")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    /**
     * 获取场站信息
     * @param gunId
     * @return
     */
    @ApiOperation("获取场站信息")
    @GetMapping("/station/info/{gunId}")
    public JsonResult getStationInfo(@PathVariable("gunId") Integer gunId) {
        log.debug("获取场站信息：{}", gunId);
        log.debug("获取场站信息调用Service的入参：{}", gunId);
        StationInfoVO stationInfoVO = deviceService.getStationInfo(gunId);
        log.debug("获取场站信息调用Service的入参：{},出参：{}", gunId, stationInfoVO);
        JsonResult jsonResult = JsonResult.ok(stationInfoVO, "获取场站信息成功");
        log.debug("获取场站信息入参：{}，出参：{}", gunId, jsonResult);
        return jsonResult;
    }

    /**
     * 更新枪的状态
     * @param gunStatusUpdateParam
     * @return
     */
    @ApiOperation("更新枪的状态")
    @PostMapping("/station/gun/status/update")
    public JsonResult updateGunStatus(@RequestBody GunStatusUpdateParam gunStatusUpdateParam) {
        log.debug("更新枪的状态入参：{}", gunStatusUpdateParam);
        log.debug("更新枪的状态调用Service的入参：{}", gunStatusUpdateParam);
        Boolean result = deviceService.updateGunStatus(gunStatusUpdateParam);
        log.debug("更新枪的状态调用Service的入参：{},出参：{}", gunStatusUpdateParam, result);
        JsonResult jsonResult = JsonResult.ok(result, "更新枪的状态成功");
        log.debug("更新枪的状态入参：{}，出参：{}", gunStatusUpdateParam, jsonResult);
        return jsonResult;
    }
}
