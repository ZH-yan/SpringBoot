package com.atdtl.repository;

import com.atdtl.entity.Basmember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 * @since 2018/7/13 18:49
 * 数据访问层接口
 */
@Repository
public interface BasmemberRepository extends JpaRepository<Basmember, Long> {

    /**
     *  根据用户名查询用户信息
     * @param username
     * @return  查询结果
     */
    List<Basmember> findAllByUsername (String username);
}
