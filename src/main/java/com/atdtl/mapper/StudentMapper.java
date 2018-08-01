package com.atdtl.mapper;

import com.atdtl.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * t_student 操作, 继承baseMapper<T>
 * @author Administrator
 * @since 2018/7/16 15:11
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    /**
     * 根据用户名统计（TODO 假设他是一个很复杂的SQL）
     * @param username 用户名
     * @return  统计结果
     */
    int countByUsername(String username);
}
