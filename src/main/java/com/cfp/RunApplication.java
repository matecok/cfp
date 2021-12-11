package com.cfp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @date 2020/01/07
 * describe:
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
public class RunApplication {
    public static void main(String[] args) {
        System.setProperty("app.start.time",System.currentTimeMillis()+"");
        SpringApplication.run(RunApplication.class, args);
    }

}
