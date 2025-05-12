package cn.tedu.charging.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        //可以配置 http连接工具, HttpUrlConnection 或者 OKHttp
        //可以配置 网络连接相关 超时时间配置
        return new RestTemplate();
    }
}
