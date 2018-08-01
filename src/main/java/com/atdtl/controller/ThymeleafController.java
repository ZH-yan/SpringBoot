package com.atdtl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Yzh on 2018/7/11-11:58.
 */
@Controller
@RequestMapping
public class ThymeleafController {

    /**
     * Spring4.3以后为简化@RequestMapping(method=ResquestMethod.XXX)的写法而做了一层包装，也就是现在的GetMappping,PostMapping...
     * @return
     */
    @GetMapping("/index")
    public ModelAndView index(){
        ModelAndView view = new ModelAndView();

        // 设置跳转的视图 默认映射到 src/main/resources/templates/{viewName}.html
        view.setViewName("index");


        // 设置属性
        view.addObject("title","我的第一个WEB页面");
        view.addObject("desc","欢迎进入DTL-WEB系统");
        Author author = new Author();
        author.setAge(22);
        author.setName("yzh");
        author.setEmail("974206359@qq.com");
        view.addObject("author",author);
        return view;
    }

    @GetMapping("/index1")
    public String index1(HttpServletRequest request){
        // TODO 与上面写法不同，但结果一致
        // 设置属性
        request.setAttribute("title","我的第一个WEB页面");
        request.setAttribute("desc","欢迎进入DTL-WEB系统");
        Author author = new Author();
        author.setAge(22);
        author.setName("yzh");
        author.setEmail("974206359@qq.com");
        request.setAttribute("author",author);
        // 返回的index 默认映射到 src/main/resources/templates/xxxx.html
        return "index";
    }


    class Author {
        private int age;
        private String name;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        private String email;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}