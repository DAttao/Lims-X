package com.example.limsweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan({"com.example.limsbase.mapper","com.example.limssys.mapper"})
@ComponentScan(basePackages = {"com.example.limssys", "com.example.limsweb","com.example.limsbase"})
public class LimsWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(LimsWebApplication.class, args);
        System.out.println("asdasdsa");
    }

}
