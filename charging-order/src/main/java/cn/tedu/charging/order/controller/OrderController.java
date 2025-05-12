package cn.tedu.charging.order.controller;

import cn.tedu.charging.common.pojo.JsonResult;
import cn.tedu.charging.order.pojo.param.OrderAddParam;
import cn.tedu.charging.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
* 订单控制器
* */


@RestController
@RequestMapping("/order")
@Slf4j
@Api(tags = "订单管理")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     * @param orderAddParam
     * @return
     */
    @ApiOperation("创建订单")
    @PostMapping("create")
    public JsonResult createOrder(@RequestBody OrderAddParam orderAddParam) {
        log.debug("创建订单入参：{}", orderAddParam);
        log.debug("订单创建调用Service的入参：{}", orderAddParam);
        String orderNo = orderService.createOrder(orderAddParam);
        log.debug("订单创建调用Service的入参：{},出参：{}", orderAddParam, orderNo);
        JsonResult jsonResult = JsonResult.ok(orderNo, "订单创建成功");
        log.debug("创建订单入参：{}，出参：{}", orderAddParam, jsonResult);
        return jsonResult;
    }
}
