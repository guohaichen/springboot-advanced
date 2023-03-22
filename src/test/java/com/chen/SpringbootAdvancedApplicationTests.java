package com.chen;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Calendar;
import java.util.Map;

//@SpringBootTest
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

    //创建token测试
    @Test
    void  createToken(){
        //现在的时间+20秒
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,2);

        String token = JWT.create()
                .withClaim("userId", 21) //payload
                .withClaim("username", "chen")//payload
                .withExpiresAt(calendar.getTime()) //设置过期时间
                .sign(Algorithm.HMAC256("#%#%FDSF@$"));//签名
        System.out.println(token);
    }

    @Test
    //根据令牌和算法解析token
    void verifyToken(){
        //secret ,算法已知
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("#%#%FDSF@$")).build();
        //insert your token
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2Nzk0NjkwNjYsInVzZXJJZCI6MjEsInVzZXJuYW1lIjoiY2hlbiJ9.kV6ZA4i_E1K0PrTqK-oCJa4JJ6BJQLafh9gZzTAsocA";
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        //根据解析的token获取payload
        Map<String, Claim> claims = decodedJWT.getClaims();
        for (Map.Entry<String, Claim> stringClaimEntry : claims.entrySet()) {
            System.out.println(stringClaimEntry.toString());
        }
    }


}
