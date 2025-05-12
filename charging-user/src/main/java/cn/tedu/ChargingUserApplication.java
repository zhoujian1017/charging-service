package cn.tedu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class ChargingUserApplication {
    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(ChargingUserApplication.class, args);
    }
}