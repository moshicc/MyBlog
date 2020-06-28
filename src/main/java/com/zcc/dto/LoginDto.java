package com.zcc.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author zcc
 * @date 2020/6/28 22:30
 * @description
 */
@Data
public class LoginDto implements Serializable {
    @NotBlank(message = "名称不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
}
