package com.lhs.service;

import com.lhs.common.PageResult;
import com.lhs.model.User;
import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(Long userId);
    User findByLoginId(String loginId);
    User login(String loginId, String password);
    boolean add(User user);
    boolean update(User user);
    boolean delete(Long userId);
    PageResult<User> findPage(int pageNum, int pageSize);
}