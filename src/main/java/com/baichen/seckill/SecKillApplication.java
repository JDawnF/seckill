package com.baichen.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date: 2019-10-05 22:32
 * @Author: baichen
 * @Description
 */
@SpringBootApplication(scanBasePackages = {"com.baichen.seckill.*"})
@RestController
@MapperScan("com.baichen.seckill.dao")
public class SecKillApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecKillApplication.class, args);
    }
}
