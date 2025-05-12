package cn.tedu.charging.order;

import cn.tedu.charging.common.pojo.JsonResult;
import cn.tedu.charging.common.pojo.vo.UserInfoVO;
import cn.tedu.charging.order.pojo.param.OrderAddParam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class RestTemplateTests {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testRestTemplate() {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://www.baidu.com", String.class);
        System.out.println(forEntity.getBody());
        System.out.println(forEntity.getStatusCode());
    }

    /**
     * 会报错 ClassCastException
     */
    @Test
    public void testRestTemplateGetUser(){
        //String url = "http://localhost:8080/user/info/11/22/333/444";
        Integer userId = 66;
        String url = "http://localhost:8080/user/info/{1}"; //占位符 ... 可变参数
        ResponseEntity<JsonResult> forEntity =
                restTemplate.getForEntity(url, JsonResult.class,userId);
        System.out.println(forEntity.getStatusCode());
        JsonResult body = forEntity.getBody();
        // 我们想要的是具体的类型UserInfoVO
        // 返回Object
        // 怎么Object 转换成UserInfoVO 强转?
        Object data = body.getData();
        //java.lang.ClassCastException:
        // java.util.LinkedHashMap cannot be cast to
        // cn.tedu.charging.common.pojo.vo.UserInfoVO
        //LinkedHashMap 不能转换为  UserInfoVO
        //https://blog.csdn.net/newchenxf/article/details/118607174
        // 无法强转
        UserInfoVO userInfoVO = (UserInfoVO)data;
        System.out.println(userInfoVO.getCarId());
    }

    /**
     * ParameterizedTypeReference 指定返回的类型,无需我们手动处理 反序列
     */
    @Test
    public void testRestTemplateExchangeGetUser(){
        Integer userId = 66;
        String url = "http://localhost:8080/user/info/{1}";
        ParameterizedTypeReference<JsonResult<UserInfoVO>> resultParameterizedTypeReference  =
                new ParameterizedTypeReference<JsonResult<UserInfoVO>>() {};
        ResponseEntity<JsonResult<UserInfoVO>> exchange =
                restTemplate.
                        exchange(url, HttpMethod.GET,
                                null,
                                resultParameterizedTypeReference,
                                userId);
        System.out.println(exchange.getStatusCode());
        JsonResult<UserInfoVO> body = exchange.getBody();
        UserInfoVO data = body.getData();
        System.out.println(data.getCarId());
    }

    /**
     * OPTION
     * GET POST PUT DELETE
     * Read Update Create Del
     *
     * HttpMethod enum
     * GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;
     */
    @Test
    public void testRestTemplatePostOrder(){
        String url = "http://localhost:7070/order/create";
        OrderAddParam orderAddParam = new OrderAddParam();
        orderAddParam.setGunId(1);
        orderAddParam.setUserId(1);
        orderAddParam.setPileId(1);
        ResponseEntity<JsonResult> entity = restTemplate
                .postForEntity(url, orderAddParam, JsonResult.class);

        System.out.println(entity.getStatusCode());
        JsonResult<UserInfoVO> body = entity.getBody();
        System.out.println(body);
    }

}
