package com.chen.exception;

import com.chen.common_service.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author cgh
 * @create 2023-04-24
 * 全局异常处理器，捕获
 */
@RestControllerAdvice
@Slf4j
public class GlobalException {
    //未验证，token失效exception
    @ExceptionHandler(AuthenticationException.class)
    public Result<?> handleAuthenticationException(AuthenticationException authenticationException){
        log.error("authenticationException : {}",authenticationException.getMessage());
        return Result.noAuth(authenticationException.getMessage());
    }

    //未授权exception
    @ExceptionHandler(AuthorizationException.class)
    public Result<?> handleAuthorizationException(AuthorizationException authorizationException){
        log.error("authorizationException : {}",authorizationException.getMessage());
        return Result.noAuth(authorizationException.getMessage());
    }

    @ExceptionHandler(ArithmeticException.class)
    public Result<?> handleArithmeticException(ArithmeticException e){
        log.error("catch ArithmeticException exception:{} ",e.getMessage());
        return Result.error(e.getMessage());
    }
}
