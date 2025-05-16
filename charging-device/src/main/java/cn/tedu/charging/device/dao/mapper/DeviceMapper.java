package cn.tedu.charging.device.dao.mapper;

import cn.tedu.charging.common.pojo.param.GunStatusUpdateParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface DeviceMapper {
    @Update("UPDATE charging_gun_info SET gun_status = #{status} WHERE id = #{gunId}")
    Boolean updateGunStatus(GunStatusUpdateParam gunStatusUpdateParam);
}
