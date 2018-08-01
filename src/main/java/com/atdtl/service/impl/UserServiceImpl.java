package com.atdtl.service.impl;

import com.atdtl.entity.User;
import com.atdtl.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @since 2018/7/16 20:17
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Map<Integer , User> DATABASES = new HashMap<>();

    static {
        DATABASES.put(1, new User(1, "u1", "p1"));
        DATABASES.put(2, new User(2, "u2", "p2"));
        DATABASES.put(3, new User(3, "u3", "p3"));
    }

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @CachePut(value = "user", key = "#user.id")
    @Override
    public User saveOrUpdate(User user) {
        DATABASES.put(user.getId(), user);
        log.info("进入saveOrUpdate方法");
        return user;
    }

    @Cacheable(value = "user", key = "#id")
    @Override
    public User get(int id) {
        // TODO: 2018/7/16 假设它是从数据库中读取出来的
        log.info("进入get方法");
        return DATABASES.get(id);
    }

    @CacheEvict(value = "user", key = "#id")
    @Override
    public void delete(int id) {
        DATABASES.remove(id);
        log.info("进入delete方法");
    }
}
