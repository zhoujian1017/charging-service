package cn.tedu.charging.device.service;


import cn.tedu.charging.common.pojo.vo.StationInfoVO;
import cn.tedu.charging.common.pojo.param.GunStatusUpdateParam;

public interface DeviceService {

    StationInfoVO getStationInfo(Integer gunId);

    Boolean updateGunStatus(GunStatusUpdateParam gunStatusUpdateParam);
}
