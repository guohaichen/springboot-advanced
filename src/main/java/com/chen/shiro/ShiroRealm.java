package com.chen.shiro;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chen.common_service.entity.UserLogin;
import com.chen.common_service.service.IUserLoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author cgh
 * @create 2023-04-10
 */
@Slf4j
@Component
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private IUserLoginService userLoginService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("shiro 权限认证");
        return null;
    }

    /**
     * 登录认证
     *
     * @param token the authentication token containing the user's principal and credentials.
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1. 获取用户信息
        UserLogin userToken = (UserLogin) token.getPrincipal();
        log.info("token coming ,user token:{}", userToken);
        //2. 调用业务层获取用户信息
        LambdaQueryWrapper<UserLogin> queryWrapper = new LambdaQueryWrapper<UserLogin>().eq(UserLogin::getPhone, userToken.getPhone());
        UserLogin one = userLoginService.getOne(queryWrapper);
        //3. 用户信息判断
        if (one != null) {
            AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                    token.getPrincipal(),
                    one.getPassword(),
                    // 指定加盐信息
                    ByteSource.Util.bytes("salt"),
                    one.getUsername()
            );
            return authenticationInfo;
        }
        return null;
    }
}
