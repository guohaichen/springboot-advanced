//package com.chen.security.login.common_service;
//
//import com.chen.common_service.entity.User;
//import lombok.Data;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//
///**
// * @author cgh
// * @create 2022-08-11 0:02
// */
//@Data
//public class LoginUser implements UserDetails {
//
//    private User user;
//
//    public LoginUser(User user) {
//        this.user = user;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }
//
//    //返回从数据库查到的password
//    @Override
//    public String getPassword() {
//        return user.getPassword();
//    }
//    //返回从数据库查到的userName
//    @Override
//    public String getUsername() {
//        return user.getUserName();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
