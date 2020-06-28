package com.zcc.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcc.common.lang.Result;
import com.zcc.entity.Blog;
import com.zcc.service.BlogService;
import com.zcc.util.ShiroUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Blob;
import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zcc
 * @since 2020-06-05
 */
@RestController
public class BlogController {
    @Autowired
    BlogService blogService;
    /**
     * 查询列表
     * @param currentPage
     * @return
     */
    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage ){

        Page page = new Page(currentPage,5);
        IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));

        return Result.succ(pageData);
    }
    /**
     * 详情
     * @param
     * @return
     */
    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable(name = "id") Long id){
        Blog blog = blogService.getById(id);
        Assert.notNull(blog,"该博客已被删除");
        return Result.succ(blog);
    }
    /**
     * 编辑(需要登录权限才能编辑)
     * RequiresAuthentication 告诉系统，这个接口需要认证之后才能访问的
     * @param
     * @return
     */
    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public Result list(@Validated @RequestBody Blog blob){
        Blog temp =null;
        if (blob.getId() != null){
            //有id 进行编辑操作
            temp = blogService.getById(blob.getId());
            //只能编辑自己的文章 ((AccountProfile) SecurityUtils.getSubject().getPrincipal().getId(); 取出当前用户)
            Assert.isTrue(temp.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(),"你没权限编辑");
        }else {
            //添加
            temp = new Blog();
            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(0);
        }
        //复制，把blog里的字段内容复制给temp相应的字段，忽略di，userId等字段
        BeanUtil.copyProperties(blob,temp,"id","userId","created","status");
        //更新和保存
        blogService.saveOrUpdate(temp);
        return Result.succ(null);
    }

}
