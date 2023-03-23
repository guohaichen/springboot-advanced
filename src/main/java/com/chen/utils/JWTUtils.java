package com.chen.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * jwt工具类，生成token，解析token，对token不做其他操作
 *
 * @author cgh
 * @create 2023-03-23
 */
@Slf4j
@Component
public class JWTUtils {

    /**
     * 生成token，token:{header.payload.signature}
     * @param username 用户名or手机号
     * @return token
     */
    public static String buildToken(String username,String secret) {
        log.info("secret:\t{}", secret);
        String token = JWT.create()
                .withClaim("username", username) //payload
                .sign(Algorithm.HMAC256(secret));//signature，
        log.info("token:\t{}", token);
        return token;
    }

    /**
     * @param token
     * @return 验证成功会反悔payload, 算法，签名，密钥任何失败都会抛出异常
     */
    public static DecodedJWT verifyToken(String token,String secret) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);

        Map<String, Claim> claims = decodedJWT.getClaims();
        claims.forEach((k, v) -> log.info("k:{}\t,v:{}", k, v));
        return decodedJWT;
    }
}
