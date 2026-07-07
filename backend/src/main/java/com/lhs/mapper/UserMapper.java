package com.lhs.mapper;

import com.lhs.model.User;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface UserMapper {
    List<User> findAll();
    User findById(@Param("userId") Long userId);
    //@Param为了给方法参数定义别名
    User findByLoginId(@Param("loginId") String loginId);
    int insert(User user);
    int update(User user);
    int deleteById(@Param("userId") Long userId);
    List<User> findPage(@Param("offset") int offset, @Param("pageSize") int pageSize);
    int countTotal();
}