package cn.tedu;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//在启动类,让spring-boot帮忙开启 FeignClients
//多个client
@EnableFeignClients
public class ChargingOrderApplication {
    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(ChargingOrderApplication.class, args);
    }
}