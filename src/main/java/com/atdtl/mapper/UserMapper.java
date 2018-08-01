package com.atdtl.mapper;

import com.atdtl.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * t_user 操作: 演示两种方式
 * <p>第一种是基于mybatis3.x版本以后提供的注解方式</>
 * <p>第二种是早期的写法，将SQL写在xml中</p>
 * @author Administrator
 * @since 2018/7/16 11:54
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户的结果集
     * @param username
     * @return 查询结果
     */
    @Select("SELECT * FROM t_user WHERE username = #{username}")
    List<User> findByUsername(@Param("username") String username);

    /**
     * 保存用户信息
     * @param user
     * @return  成功  1   失败  0
     */
    int insert(User user);
}
