package cn.tedu.charging.order.fegin;

import cn.tedu.charging.common.pojo.JsonResult;
import cn.tedu.charging.common.pojo.param.GunStatusUpdateParam;
import cn.tedu.charging.common.pojo.vo.StationInfoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("deviceService")
public interface DeviceClient {

    /**
     * 获取站点信息
     * @param gunId
     * @return
     */
    @GetMapping("/device/station/info/{gunId}")
    JsonResult<StationInfoVO> getStationInfo(@PathVariable("gunId") Integer gunId);

    /**
     * 更新枪状态
     * @param gunStatusUpdateParam
     * @return
     */
    @PostMapping("/device/station/gun/status/update")
    JsonResult<Boolean> updateGunStatus(@RequestBody GunStatusUpdateParam gunStatusUpdateParam);
}
