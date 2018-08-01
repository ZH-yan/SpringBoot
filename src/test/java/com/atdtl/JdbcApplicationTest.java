package com.atdtl;

import com.atdtl.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author Administrator
 * @since 2018/7/12 10:42
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Chapter1Application.class , webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JdbcApplicationTest {

    private static final Logger log = LoggerFactory.getLogger(Chapter1Application.class);

    @Autowired
    private TestRestTemplate template;
    @LocalServerPort
    private int port;

    @Test
    public void test1() throws Exception{
        String url = "http://localhost:" + port + "/dev/users";

        template.postForEntity(url, new User("admin", "654321"), Integer.class);
        template.postForEntity(url, new User("user1","passw1") , Integer.class);
        log.info("[添加用户成功]\n");

        // TODO 如果返回的是集合,要用exchange 而不是getForEntity,后者需要自己强转类型
        ResponseEntity<List<User>> responseEntity = template.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {});
        final List<User> body = responseEntity.getBody();
        log.info("[查询所有] - [{}]\n" , body);

        int id = body.get(0).getId();
        ResponseEntity<User> responseEntity1 = template.getForEntity(url + "/{id}", User.class, id);
        log.info("[主键查询] - [{}]\n" , responseEntity1.getBody());

        template.put(url + "/{id}", new User("admin","admin"), id);
        log.info("[修改用户成功]\n");

        ResponseEntity<List<User>> responseEntity2 = template.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {});
        log.info("[查询所有] - [{}]\n" , responseEntity2.getBody());

        template.delete(url + "/{id}", body.get(1).getId());
        log.info("[删除用户成功]\n");

        ResponseEntity<List<User>> responseEntity3 = template.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {});
        log.info("[查询所有] - [{}]\n" , responseEntity3.getBody());
    }
}
