package com.atdtl.service;

import com.atdtl.entity.User;

/**
 * @author Administrator
 * @since 2018/7/16 20:14
 */
public interface UserService {

    /**
     * 添加或更新
     *
     * @param user
     * @return
     */
    User saveOrUpdate(User user);

    /**
     * 查询
     *
     * @param id
     * @return
     */
    User get(int id);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    void delete(int id);
}
