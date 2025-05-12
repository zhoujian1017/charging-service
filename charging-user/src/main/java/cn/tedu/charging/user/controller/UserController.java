package cn.tedu.charging.user.controller;

import cn.tedu.charging.common.pojo.JsonResult;
import cn.tedu.charging.common.pojo.vo.UserInfoVO;
import cn.tedu.charging.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/*
* 订单控制器
* */


@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户接口")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 创建订单
     * @param userId
     * @return
     */
    @ApiOperation("获取用户信息")
    @GetMapping("/info/{userId}")
    public JsonResult getUserInfo(@PathVariable("userId") Integer userId) {
        log.debug("获取用户信息入参：{}", userId);
        log.debug("获取用户信息调用Service的入参：{}", userId);
        UserInfoVO userInfoVO = userService.getUserInfo(userId);
        log.debug("获取用户信息调用Service的入参：{},出参：{}", userId, userInfoVO);
        JsonResult jsonResult = JsonResult.ok(userInfoVO, "获取用户信息成功");
        log.debug("创建订单入参：{}，出参：{}", userId, jsonResult);
        return jsonResult;
    }
}
