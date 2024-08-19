package com.example.demo01;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EntityScan(basePackages = "com.example.demo01.entity")

class Demo01ApplicationTests {

    @Test
    void contextLoads() {
    }

}

