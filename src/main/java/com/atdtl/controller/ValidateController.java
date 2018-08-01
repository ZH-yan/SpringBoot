package com.atdtl.controller;

import com.atdtl.annotation.DateTime;
import com.atdtl.entity.Book;
import com.atdtl.validator.groups.Groups;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 *  参数校验
 *
 * @author Administrator
 * @since 2018/7/27 13:35
 */
@Validated
@RestController
public class ValidateController {

    @GetMapping("/test2")
    public String test2(@NotBlank(message = "name 不能为空") @Length(min = 2, max = 10, message = "长度必须介于${min}-${max}之间") String name) {
        return "success";
    }

    @GetMapping("/test3")
    public String test3(@Validated Book book) {
        return "success";
    }

    /**
     * 自定义注解异常
     * @param date
     * @return
     */
    @GetMapping("/test")
    public String test(@DateTime(message = "格式错误，正确格式为:{format}", format = "yyyy-MM-dd HH:mm") String date){
        return "success";
    }

    /**
     * 分组下的数据校验
     *      insert 时，ID不需关心是否为空
     *      update 时，ID 不能为空
     *
     * @param book
     * @return
     */
    @GetMapping("/insert")
    public String insert(@Validated(value = Groups.Default.class) Book book){
        return "success";
    }
    @GetMapping("/update")
    public String update(@Validated(value = Groups.Update.class) Book book){
        return "success";
    }

}
