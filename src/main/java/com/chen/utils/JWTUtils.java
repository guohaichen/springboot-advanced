package com.chen.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
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
    public static String buildToken(String username, String secret) {
        log.info("secret:\t{}", secret);
        String token = JWT.create()
                .withClaim("username", username) //payload
                .sign(Algorithm.HMAC256(secret));//signature，
        log.info("token:\t{}", token);
        return token;
    }

    /**
     * @param token
     */
    public static void verifyToken(String token, String secret) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
        DecodedJWT decodedJWT = null;
        try {
            decodedJWT = jwtVerifier.verify(token);
        } catch (InvalidClaimException e) {
            throw new InvalidClaimException("用户信息错误!");
        }
        //payload信息
        Map<String, Claim> claims = decodedJWT.getClaims();
//        claims.forEach((k, v) -> log.info("k:{}\t,v:{}", k, v));
    }

    public static String getUserName(String token){
        try {
            DecodedJWT jwt = JWT.decode(token);
            log.info("jwt.getClaim(username).asString: {}",jwt.getClaim("username").asString());
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            throw new RuntimeException("无用户名错误");
        }
    }
}
