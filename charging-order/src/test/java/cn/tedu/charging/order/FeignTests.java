package cn.tedu.charging.order;

import cn.tedu.charging.common.pojo.JsonResult;
import cn.tedu.charging.common.pojo.vo.UserInfoVO;
import cn.tedu.charging.order.fegin.UserClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FeignTests {
    @Autowired
    private UserClient userClient;

    @Test
    public void testFeign() {
        JsonResult<UserInfoVO> userCarInfo = userClient.getUserCarInfo(1);
        UserInfoVO data = userCarInfo.getData();
        System.out.println(data);
    }
}
