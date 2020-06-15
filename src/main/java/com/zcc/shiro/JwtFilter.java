package com.zcc.shiro;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zcc.common.lang.Result;
import com.zcc.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zcc
 * @date 2020/6/15 22:01
 * @description 继承shiro内置的 过滤器 AuthenticatingFilter 或者BaseicHttpAuthenticationFilter
 * 通过AuthenticatingFilter 里面的方法createToken 拿到token ，再通过AuthenticatingFilter 里面的方法：
 * Subject subject = this.getSubject(request, response);
 *                 subject.login(token);  交给shiro进行登录的处理，最后委托给realm进行处理（就是我们自定义的AccountRealm处理）
 */
@Component
public class JwtFilter extends AuthenticatingFilter {
    @Autowired
    JwtUtils jwtUtils;
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request =(HttpServletRequest) servletRequest;
        String jwt= request.getHeader("Authorization");
        //如果请求头里面没有jwt，就直接返回null
        if (StringUtils.isEmpty(jwt)){
            return null;
        }
        return new JwtToken(jwt);
    }
    //过滤 拦截用户请求 ，里面的jwt 进行判断
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        // 获取到jwt 之后 判断 jwt是否错误，过期。。。
        HttpServletRequest request =(HttpServletRequest) servletRequest;
        String jwt= request.getHeader("Authorization");
        if (StringUtils.isEmpty(jwt)){
            //当它为空的时候，不需要交给shiro 进行登录处理。直接交给注解进行拦截
            return true;
        }else {
            //1.有jwt ,進行校驗 null 或者过期
            Claims claim = jwtUtils.getClaimByToken(jwt);
            if (claim ==null || jwtUtils.isTokenExpired(claim.getExpiration())){
                throw  new ExpiredCredentialsException("token已失效，请重新登录");
            }
            //2.登录处理
            return executeLogin(servletRequest,servletResponse);

        }
    }
    //因为封装了统一的返回，所以要重写异常(执行登录，出现异常进行逻辑处理)
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        Throwable throwable = e.getCause() == null ? e : e.getCause();
        Result result = Result.fail(throwable.getMessage());
        String json = JSONUtil.toJsonStr(result);
        try {
            httpServletResponse.getWriter().print(json);
        } catch (IOException ex) {

        }
        return false;
    }
}
