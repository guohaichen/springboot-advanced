package com.chen.shiro;

import com.chen.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author cgh
 * @create 2023-04-13
 * 验证token与验证 realm
 */
@Component
@Slf4j
public class TokenValidateAndAuthenticationFilter extends AuthorizingRealm {

    @Value("${token.secret}")
    private static String tokenSecret;

    public TokenValidateAndAuthenticationFilter() {
        //自定义匹配策略，验证jwt token的策略
        super(new CredentialsMatcher() {
            @Override
            public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
                log.info("doCredential token合法性验证");
                BearerToken bearerToken = (BearerToken) token;
                String bearerTokenString = bearerToken.getToken();
                log.debug("bearerToken: {}", bearerTokenString);
                try {
                    JWTUtils.verifyToken(bearerTokenString, tokenSecret);
                    return true;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        return null;
    }
}
