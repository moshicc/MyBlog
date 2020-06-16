package com.zcc.controller;


import com.zcc.common.lang.Result;
import com.zcc.entity.User;
import com.zcc.service.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zcc
 * @since 2020-06-05
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

//RequiresAuthentication 注解，需要进行登录，才能访问/index
    @RequiresAuthentication
    @GetMapping("/index")
    public Result index(){
        User user = userService.getById(1L);
        return Result.succ(user);
    }

    @PostMapping("/insert")
    public Result insert(@Validated @RequestBody User user){
//        userService.save(user);
        return Result.succ(user);
    }

}
