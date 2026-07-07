package com.lhs.controller;

import com.lhs.common.PageResult;
import com.lhs.common.ResultBean;
import com.lhs.model.User;
import com.lhs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
//等价 @Controller + @ResponseBody
@RequestMapping("/api/admin/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")

    //只接收 GET 方式的请求，并把请求路径绑定到当前方法

    public ResultBean list(@RequestParam(defaultValue = "1") int pageNum,
                           @RequestParam(defaultValue = "10") int pageSize) {
        //@RequestParam：获取URL 问号后面参数
        PageResult<User> pageResult = userService.findPage(pageNum, pageSize);
        return ResultBean.ok().data(pageResult);
    }

    @GetMapping("/{id}")
    public ResultBean getById(@PathVariable Long id) {
        //@PathVariable：获取路径占位符{id}里的值
        User user = userService.findById(id);
        if (user != null) {
            user.setPassword(null);
            return ResultBean.ok().data(user);
        }
        return ResultBean.error().message("用户不存在");
    }

    @PostMapping("/add")
    public ResultBean add(@RequestBody User user) {
        if (userService.findByLoginId(user.getLoginId()) != null) {
            return ResultBean.error().message("账号已存在");
        }
        if (userService.add(user)) {
            return ResultBean.ok().message("添加成功");
        }
        return ResultBean.error().message("添加失败");
    }

    @PutMapping("/edit")

    //@PutMapping：规范用于修改操作

    public ResultBean edit(@RequestBody User user) {
        if (userService.update(user)) {
            return ResultBean.ok().message("修改成功");
        }
        return ResultBean.error().message("修改失败");
    }

    @DeleteMapping("/delete/{id}")
    public ResultBean delete(@PathVariable Long id) {
        if (userService.delete(id)) {
            return ResultBean.ok().message("删除成功");
        }
        return ResultBean.error().message("删除失败");
    }

    @PutMapping("/reset-password")
    public ResultBean resetPassword(@RequestParam Long userId) {
        User user = userService.findById(userId);
        if (user != null) {
            user.setPassword("123456");
            userService.update(user);
            return ResultBean.ok().message("密码已重置为123456");
        }
        return ResultBean.error().message("用户不存在");
    }
}