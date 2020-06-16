package com.zcc.common.exception;

import com.zcc.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author zcc
 * @date 2020/6/16 21:02
 * @description 全局异常捕获类
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //捕获Shiro的异常,UNAUTHORIZED 401  没有权限
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = ShiroException.class)
    public Result handler(ShiroException e){
        log.error("shiro异常：-----------------{}",e.getMessage());
        Result errMsg = Result.fail(401,e.getMessage(),null);
        return errMsg;
    }

    //所有的RunntimeException 都会被捕获 ，交给handler处理，返回一个异常信息封装了Result，
    // （如：XXXController出现异常时，就会返回一个Result给前台，这和Result正好是我们统一返回的格式）
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(RuntimeException e){
        log.error("运行时异常：-----------------{}",e.getMessage());
        Result errMsg = Result.fail(e.getMessage());
        return errMsg;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException e){
        log.error("实体校验异常：-----------------{}",e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        //getAllErrors得到的是一个Error集合，用stream获取集合串行流，再找个第一个，在取得
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        //最后把取得的第一个错误信息 放到Result中
        Result errMsg = Result.fail(objectError.getDefaultMessage());
        return errMsg;
    }


}
