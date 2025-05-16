package cn.tedu.charging.device.service.impl;

import cn.tedu.charging.common.pojo.vo.StationInfoVO;
import cn.tedu.charging.common.pojo.param.GunStatusUpdateParam;
import cn.tedu.charging.device.dao.mapper.DeviceMapper;
import cn.tedu.charging.device.dao.repository.DeviceRepository;
import cn.tedu.charging.device.service.DeviceService;
import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public StationInfoVO getStationInfo(Integer gunId) {
        StationInfoVO stationInfoVO = new StationInfoVO();
        stationInfoVO.setStationId(777);
        stationInfoVO.setOperatorId(111);
        return stationInfoVO;
    }

    @Override
    public Boolean updateGunStatus(GunStatusUpdateParam gunStatusUpdateParam) {
        log.info("更新枪口状态：{}", gunStatusUpdateParam);
        Boolean result = deviceMapper.updateGunStatus(gunStatusUpdateParam);
        return result;
    }
}
