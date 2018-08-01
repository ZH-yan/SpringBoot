package com.atdtl;

import com.atdtl.entity.Basmember;
import com.atdtl.repository.BasmemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author Administrator
 * @since 2018/7/13 18:54
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JPADataApplicationTest {

    private static final Logger log = LoggerFactory.getLogger(JPADataApplicationTest.class);

    @Autowired
    private BasmemberRepository basmemberRepository;

    @Test
    public void test1(){
        final Basmember basmember = basmemberRepository.save(new Basmember("admin", "admin"));
        log.info("[添加成功] - [{}]", basmember);

        final List<Basmember> basmembers = basmemberRepository.findAllByUsername("admin");
        log.info("[条件查询] - [{}]", basmembers);

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("username")));
        final Page<Basmember> basmemberPage = basmemberRepository.findAll(pageRequest);
        log.info("[分页+排序+查询所有] - [{}]", basmemberPage.getContent());

        basmemberRepository.findById(basmemberPage.getContent().get(0).getId()).ifPresent(basmember1 -> log.info("[主键查询] - [{}]", basmember1));

        final Basmember editBas = basmemberRepository.save(new Basmember(basmember.getId(), "修改后的username", "修改后的password"));
        log.info("[修改成功] - [{}]", editBas);

        basmemberRepository.deleteById(basmember.getId());
        log.info("[删除主键为 {} 成功] - [{}]", basmember.getId());
    }
}
