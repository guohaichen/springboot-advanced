package com.chen.common_service.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequestMapping("/auth")
@RestController
public class LoginController {

    @PostMapping("/login")
    public Boolean userLogin(@RequestBody Map<String, String> paramsMap) {
        //模拟登陆
        String username = paramsMap.get("username");
        String password = paramsMap.get("password");
        log.info("request in ,username:{},password:{}", username, password);
        return "admin".equals(username) && "123456".equals(password);
    }
}
