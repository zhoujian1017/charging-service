package cn.tedu.charging.device.service.impl;

import cn.tedu.charging.common.pojo.vo.StationInfoVO;
import cn.tedu.charging.common.pojo.param.GunStatusUpdateParam;
import cn.tedu.charging.device.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService {

    @Override
    public StationInfoVO getStationInfo(Integer gunId) {
        StationInfoVO stationInfoVO = new StationInfoVO();
        stationInfoVO.setStationId(777);
        stationInfoVO.setOperatorId(111);
        return stationInfoVO;
    }

    @Override
    public Boolean updateGunStatus(GunStatusUpdateParam gunStatusUpdateParam) {
        return true;
    }
}
