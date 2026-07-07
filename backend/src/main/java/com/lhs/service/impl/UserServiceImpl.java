package com.lhs.service.impl;

import com.lhs.common.PageResult;
import com.lhs.mapper.UserMapper;
import com.lhs.model.User;
import com.lhs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
//@Service 是 Spring 注解，作用：把当前实现类创建对象存入 IOC 容器，方便后续用 @Autowired 自动注入获取对象。
//Spring 启动时，@Service 标注的 UserServiceImpl 会被存入 IOC 容器；
// @Autowired 自动从容器取出该实例赋值给 userService。
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;//依靠 Mapper 调用 SQL。

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    public User findById(Long userId) {
        return userMapper.findById(userId);
    }

    @Override
    public User findByLoginId(String loginId) {
        return userMapper.findByLoginId(loginId);
    }

    @Override
    public User login(String loginId, String password) {
        User user = userMapper.findByLoginId(loginId);
        if (user != null && password.equals(user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public boolean add(User user) {
        return userMapper.insert(user) > 0;
    }

    @Override
    public boolean update(User user) {
        return userMapper.update(user) > 0;
    }

    @Override
    public boolean delete(Long userId) {
        return userMapper.deleteById(userId) > 0;
    }

    @Override
    public PageResult<User> findPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<User> list = userMapper.findPage(offset, pageSize);
        int total = userMapper.countTotal();
        return new PageResult<>(pageNum, pageSize, total, list);
    }
}