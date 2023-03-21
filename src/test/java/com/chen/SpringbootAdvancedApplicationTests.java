package com.chen;

import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootAdvancedApplicationTests {

    //加密测试
    @Test
    void checkPassword(){
        //hash password
        String password = "cm129911";
        String genSalt = "$2a$10$bsNHD51BJgCuW0nFOo.6de";
        System.out.println("盐:\t"+genSalt);
        String hashPwd = BCrypt.hashpw(password, genSalt);
        System.out.println(hashPwd);

        String userPassword = "cm129911";
        String pwd = BCrypt.hashpw(userPassword, genSalt);
        System.out.println(pwd);

        System.out.println(hashPwd.equals(pwd));

    }

}
