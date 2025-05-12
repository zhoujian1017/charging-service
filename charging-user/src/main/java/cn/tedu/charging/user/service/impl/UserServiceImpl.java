package cn.tedu.charging.user.service.impl;

import cn.tedu.charging.common.pojo.vo.UserInfoVO;
import cn.tedu.charging.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Override
    public UserInfoVO getUserInfo(Integer userId) {
        //应该从数据库获取
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setCarId(16);
        return userInfoVO;
    }
}
