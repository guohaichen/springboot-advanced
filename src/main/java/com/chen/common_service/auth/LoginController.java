package com.chen.common_service.auth;

import com.chen.common_service.dto.Result;
import com.chen.common_service.service.IUserLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@RequestMapping("/auth")
@RestController
public class LoginController {

    @Resource
    private IUserLoginService iUserLoginService;

    @PostMapping("/login")
    public Result<?> userLogin(@RequestBody Map<String, String> paramsMap) {
        String username = paramsMap.get("username");
        String password = paramsMap.get("password");
        log.info("request in ,username:{},password:{}", username, password);
        //v1:未加密且直接从数据库验证
        if (iUserLoginService.validatePassword(username, password)) {
            return Result.OK(username);
        } else return Result.error("密码错误");
    }
}
