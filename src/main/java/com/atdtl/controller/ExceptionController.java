package com.atdtl.controller;

import com.atdtl.exception.MyException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 全局异常演示
 *
 * @author Administrator
 * @since 2018/7/26 19:16
 */
@RestController
public class ExceptionController {

    @GetMapping("/test1")
    public String test1(Integer num) {
        // TODO: 2018/7/26 通过@RequestParam(required = true) 便可以控制
        if (num == null) {
            throw new MyException("num不能为空", 400);
        }

        int i = 10 / num;
        return "result:" + i;
    }
}
