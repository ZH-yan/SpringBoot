package com.atdtl.entity;

/**
 * @author Administrator
 * @since 2018/8/1 14:44
 */
public class Employee {

    /**
     *  自增ID
     */
    private Long id;

    /**
     *  用户名
     */
    private String username;

    /**
     *  密码
     */
    private String password;

    /**
     *  角色名：shiro支持多个角色，而且接收的参数也是 set<String> 集合
     */
    private String roleName;

    /**
     *  是否禁用
     */
    private boolean locked;

    public Employee() {
    }

    public Employee(Long id, String username, String password, String roleName, boolean locked) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roleName = roleName;
        this.locked = locked;
    }

    public Employee(String username, String password, String roleName, boolean locked) {
        this.username = username;
        this.password = password;
        this.roleName = roleName;
        this.locked = locked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public String toString() {
        return "Employee{}";
    }
}
