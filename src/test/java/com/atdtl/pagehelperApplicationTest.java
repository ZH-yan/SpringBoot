package com.atdtl;

import com.atdtl.entity.Student;
import com.atdtl.mapper.StudentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Administrator
 * @since 2018/7/16 15:21
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class pagehelperApplicationTest {

    private static final Logger log = LoggerFactory.getLogger(pagehelperApplicationTest.class);

    @Autowired
    private StudentMapper studentMapper;

    @Test
    public void test1(){
        final Student student1 = new Student("u1", "p1");
        final Student student2 = new Student("u1", "p2");
        final Student student3 = new Student("u3", "p3");

        studentMapper.insert(student1);
        log.info("[user1回写主键] - [{}]", student1.getId());
        studentMapper.insert(student2);
        log.info("[user1回写主键] - [{}]", student2.getId());
        studentMapper.insert(student3);
        log.info("[user1回写主键] - [{}]", student3.getId());

        int counts = studentMapper.countByUsername("u1");
        log.info("[调用自己写的SQL] - [{}]", counts);

        // TODO: 2018/7/16 模拟分页
        studentMapper.insert(new Student("u1", "p1"));
        studentMapper.insert(new Student("u1", "p2"));
        studentMapper.insert(new Student("u3", "p3"));
        studentMapper.insert(new Student("u1", "p1"));
        studentMapper.insert(new Student("u1", "p2"));
        studentMapper.insert(new Student("u3", "p3"));
        studentMapper.insert(new Student("u1", "p1"));
        studentMapper.insert(new Student("u1", "p2"));
        studentMapper.insert(new Student("u3", "p3"));
        studentMapper.insert(new Student("u1", "p1"));
        studentMapper.insert(new Student("u1", "p2"));
        studentMapper.insert(new Student("u3", "p3"));
        studentMapper.insert(new Student("u1", "p1"));
        studentMapper.insert(new Student("u1", "p2"));
        studentMapper.insert(new Student("u3", "p3"));
        studentMapper.insert(new Student("u1", "p1"));
        studentMapper.insert(new Student("u1", "p2"));
        studentMapper.insert(new Student("u3", "p3"));
        studentMapper.insert(new Student("u1", "p1"));
        studentMapper.insert(new Student("u1", "p2"));
        studentMapper.insert(new Student("u3", "p3"));
        studentMapper.insert(new Student("u1", "p1"));
        studentMapper.insert(new Student("u1", "p2"));
        studentMapper.insert(new Student("u3", "p3"));
        studentMapper.insert(new Student("u1", "p1"));
        studentMapper.insert(new Student("u1", "p2"));
        studentMapper.insert(new Student("u3", "p3"));
        studentMapper.insert(new Student("u1", "p1"));
        studentMapper.insert(new Student("u1", "p2"));
        studentMapper.insert(new Student("u3", "p3"));

        // TODO: 2018/7/16 分页+排序 this.studentMapper.selectAll()
        final PageInfo<Object> pageInfo = PageHelper.startPage(1, 10).setOrderBy("id desc").doSelectPageInfo(() -> this.studentMapper.selectAll());
        log.info("[lambda写法] - [分页信息] - [{}]", pageInfo.toString());

        PageHelper.startPage(1,10).setOrderBy("id desc");
        PageInfo<Student> studentPageInfo = new PageInfo<>(this.studentMapper.selectAll());
        log.info("[普通写法] - {[]}", studentPageInfo);

    }
}
