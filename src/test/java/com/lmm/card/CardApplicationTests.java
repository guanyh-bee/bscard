package com.lmm.card;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.MD5;
import org.apache.tomcat.util.security.MD5Encoder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CardApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(DigestUtil.bcrypt("123456"));
    }

}
