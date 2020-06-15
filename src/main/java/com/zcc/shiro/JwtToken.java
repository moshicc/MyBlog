package com.zcc.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author zcc
 * @date 2020/6/15 22:08
 * @description 自己写个token 实现AuthenticationToken ，给JwtFilter使用
 */

public class JwtToken implements AuthenticationToken {
    private String token;

    public JwtToken(String jwt){
        this.token = jwt;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
