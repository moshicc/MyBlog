package com.zcc.shiro;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zcc
 * @date 2020/6/15 23:16
 * @description
 */
@Data
public class AccountProfile implements Serializable {
    private Long id;

    private String username;

    private String avatar;

    private String email;
}
