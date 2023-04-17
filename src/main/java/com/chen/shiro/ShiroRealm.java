package com.chen.shiro;

import com.chen.common_service.service.IUserLoginService;
import com.chen.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author cgh
 * @create 2023-04-10
 * 自定义Realm 重写认证和授权的方法
 */
@Slf4j
@Component
public class ShiroRealm extends AuthorizingRealm {

    @Value("${token.secret}")
    private String secret;

    @Autowired
    private IUserLoginService userLoginService;

    /**
     * 必须重写此方法，否则shiro不会走自定义realm,报错如下：(一步步debug出来的，😭😭😭😭)
     * if (!realm.supports(token)) {
     *  String msg = "Realm [" + realm + "] does not support authentication token [" + token + "].  Please ensure that the appropriate Realm implementation is configured correctly or that the realm accepts AuthenticationTokens of this type.";
     *  throw new UnsupportedTokenException(msg);
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("shiro 权限认证");
        return null;
    }

    /**
     * 登录认证
     * subject.login时 会一步一步委托到Realm的doGetAuthenticationInfo方法来。
     *
     * @param token the authentication token containing the user's principal and credentials.
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //0. 拿到token
        String tokenString = token.getPrincipal().toString();
        //1. 验证token
        if (JWTUtils.verifyToken(tokenString,secret)) {
            //验证成功
            String username =  JWTUtils.getUserName(tokenString);
            log.info("token authenticate, user's username: {}", username);
            if (username != null ) {
                //shiro默认的AuthenticatingRealm会对token进行校验，这里需要设置一致
                return new SimpleAuthenticationInfo(
                        tokenString, tokenString, getName()
                );
            }
        }
        return null;
    }
}
