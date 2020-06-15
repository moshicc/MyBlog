package com.zcc.controller;


import com.zcc.common.lang.Result;
import com.zcc.entity.User;
import com.zcc.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/index")
    public Result index(){
        User user = userService.getById(1L);
        return Result.succ(user);
    }

    @PostMapping("/insert/{username}")
    public void insert(@PathVariable("username") String username){
        User user = new User();
        user.setUsername(username);
        user.setStatus(1);
        userService.save(user);
    }

}
