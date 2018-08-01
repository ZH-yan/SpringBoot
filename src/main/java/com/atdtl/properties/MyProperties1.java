package com.atdtl.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 *  定义MyProperties1.java文件，来映射我们在 application.properties中的内容 ，即通过操作对象的方式来获取配置文件的内容
 * Created by Yzh on 2018/7/10-20:25.
 */
@Component
@ConfigurationProperties(prefix = "myl")
public class MyProperties1 {

    private int age;
    private String name;

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

    @Override
    public String toString() {
        return "MyProperties1{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
