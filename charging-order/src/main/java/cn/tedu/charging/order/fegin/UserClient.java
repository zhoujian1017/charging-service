package cn.tedu.charging.order.fegin;

import cn.tedu.charging.common.pojo.JsonResult;
import cn.tedu.charging.common.pojo.vo.UserInfoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "userService")
public interface UserClient {

    @GetMapping(value = "/user/info/{userId}")
    JsonResult<UserInfoVO> getUserCarInfo(@PathVariable("userId") Integer userId);
}
