package cn.chen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching // 加这个注解开启缓存
@SpringBootApplication
public class BaseOneApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseOneApplication.class, args);
    }

}
