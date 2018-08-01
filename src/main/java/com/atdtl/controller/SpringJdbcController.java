package com.atdtl.controller;

import com.atdtl.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Administrator
 * @since 2018/7/12 10:19
 */
@RestController
@RequestMapping("/usersjdbc")
public class SpringJdbcController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SpringJdbcController(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @PostMapping
    public int addUser(@RequestBody User user){
        // 添加用户
        String sql = "insert into t_user(username, password) values(?, ?)";
        return jdbcTemplate.update(sql, user.getUsername(), user.getPassword());
    }

    @DeleteMapping("/{id}")
    public int delUser(@PathVariable Long id){
        // 根据主键ID删除用户信息
        String sql = "delete from t_user where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @PutMapping("/{id}")
    public int updateUser(@PathVariable Long id, @RequestBody User user){
        // 根据主键ID修改用户信息
        String sql = "update t_user set username = ? ,password = ? where id = ?";
        return jdbcTemplate.update(sql ,user.getUsername() ,user.getPassword(), id);
    }

    @GetMapping
    public List<User> queryUsers(){
        // 查询所有用户
        String sql = "select * from t_user";
        return jdbcTemplate.query(sql,new Object[]{},new BeanPropertyRowMapper<>(User.class));
    }

    @GetMapping("/{id}")
    public User queryById(@PathVariable Long id){
        // 根据主键ID查询
        String sql = "select * from t_user where id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<>(User.class));
    }
}
