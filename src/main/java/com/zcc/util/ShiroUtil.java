package com.zcc.util;

import com.zcc.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

/**
 * @author zcc
 * @date 2020/6/28 23:39
 * @description
 */

public class ShiroUtil {

    public static AccountProfile getProfile(){
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }
}
