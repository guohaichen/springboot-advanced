package com.chen.shiro;

import com.chen.utils.JWTUtils;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author cgh
 * @create 2023-04-17
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    public JwtToken() {
    }

    @Override
    public Object getPrincipal() {
        return getToken();
    }

    @Override
    public Object getCredentials() {
        return getToken();
    }

    public String getUsername() {
        return JWTUtils.getUserName(token);
    }

    public String getToken() {
        return token;
    }
}
