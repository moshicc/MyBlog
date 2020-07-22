package com.zcc.util;

import cn.hutool.crypto.digest.DigestUtil;

/**
 * @author zcc
 * @ClassName DigestUtils
 * @description 借助apache工具类DigestUtils实现
 * @date 2020/7/22 9:59
 * @Version 1.0
 */

public class DigestUtils {
    public static void main(String[] args) {

        String str = "123456";
        //MD5加密
        String digestStr = DigestUtil.md5Hex(str);
        System.out.printf(digestStr);
    }
}
