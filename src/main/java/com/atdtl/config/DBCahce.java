package com.atdtl.config;

import com.atdtl.entity.Employee;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @since 2018/8/1 14:51
 */
public class DBCahce {

    /**
     *  用户名 用户信息
     */
    public static final Map<String, Employee> EMPLOYEE_CACHE = new HashMap<>();

    /**
     *  角色ID  权限编码
     */
    public static final Map<String, Collection<String>> PERMISSIONS_CACHE = new HashMap<>();

    static {
        // TODO 假设这是数据库记录
        EMPLOYEE_CACHE.put("u1", new Employee(1L, "u1", "p1", "admin", true));
        EMPLOYEE_CACHE.put("u2", new Employee(2L, "u2", "p2", "admin", false));
        EMPLOYEE_CACHE.put("u3", new Employee(3L, "u3", "p3", "test", true));

        PERMISSIONS_CACHE.put("admin", Arrays.asList("employee:list", "employee:add", "employee:edit"));
        PERMISSIONS_CACHE.put("test", Collections.singletonList("employee:list"));

    }
}
