package com.zcc.shiro;

import com.zcc.entity.User;
import com.zcc.service.UserService;
import com.zcc.util.JwtUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zcc
 * @date 2020/6/15 21:44
 * @description 继承AuthorizingRealm  重写他的两个方法
 */
@Component
public class AccountRealm extends AuthorizingRealm {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserService userService;

    //告诉我们的Realm支持的是JwtToken而不是其他的token
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    //拿到用户后，获取他的权限，封装成AuthorizationInfo 返回 给shiro
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }
    //获取身份，进行token 验证，如 密码校验

    /**
     * Subject subject = this.getSubject(request, response);
     * subject.login(token);
     * @param
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        JwtToken jwtToken = (JwtToken) token;
        String userId = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();
        User user = userService.getById(Long.valueOf(userId));
        if (user ==null){
            throw new UnknownAccountException("账户不存在");
        }

        if (user.getStatus() == -1){
            throw  new LockedAccountException("账户已被锁定");
        }
        AccountProfile profile = new AccountProfile();
        BeanUtils.copyProperties(user,profile);

        System.out.print("-------------------------------");
        //principal, credentials,  realmName ..返回一些非私密的基本信息，用个封装类AccountProfile
        return new SimpleAuthenticationInfo(profile,jwtToken.getCredentials(),getName());
    }
}
