package com.lhs.mapper;

import com.lhs.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testFindAll() {
        List<User> users = userMapper.findAll();
        System.out.println("用户总数：" + users.size());
        for (User user : users) {
            System.out.println("用户：" + user.getUserName() + "，账号：" + user.getLoginId());
        }
    }

    @Test
    public void testFindById() {
        User user = userMapper.findById(1L);
        if (user != null) {
            System.out.println("找到用户：" + user.getUserName());
        } else {
            System.out.println("用户不存在");
        }
    }

    @Test
    public void testFindByLoginId() {
        User user = userMapper.findByLoginId("admin");
        if (user != null) {
            System.out.println("找到用户：" + user.getUserName());
        } else {
            System.out.println("用户不存在");
        }
    }

    @Test
    public void testInsert() {
        User user = new User();
        user.setLoginId("test001");
        user.setPassword("123456");
        user.setUserName("测试用户");
        user.setMobile("13800138000");
        user.setEmail("test@example.com");
        user.setUserType(0);
        user.setStatus(0);

        int result = userMapper.insert(user);
        System.out.println("插入结果：" + (result > 0 ? "成功" : "失败"));
    }

    @Test
    public void testUpdate() {
        User user = userMapper.findById(1L);
        if (user != null) {
            user.setMobile("13900139000");
            int result = userMapper.update(user);
            System.out.println("更新结果：" + (result > 0 ? "成功" : "失败"));
        }
    }

    @Test
    public void testDeleteById() {
        int result = userMapper.deleteById(100L);
        System.out.println("删除结果：" + (result > 0 ? "成功" : "失败（用户不存在）"));
    }

    @Test
    public void testFindPage() {
        List<User> users = userMapper.findPage(0, 5);
        System.out.println("分页查询（第1页，每页5条）：" + users.size() + "条记录");
    }

    @Test
    public void testCountTotal() {
        int count = userMapper.countTotal();
        System.out.println("用户总数：" + count);
    }
}