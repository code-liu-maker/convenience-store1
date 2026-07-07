package com.lhs.login.controller;

import com.lhs.common.ResultBean;
import com.lhs.model.User;
import com.lhs.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
// @RestController=@Controller + @ResponseBody;@Controller标识当前类是 MVC 控制器，接收前端请求
@RequestMapping("/api")
//写在类上方，给这个类里面所有接口统一加上路径前缀 /api
public class LoginController {

    @Autowired
    //@Autowired 自动依赖注入
    //Spring IOC 容器自动查找 UserService 的实现类对象，自动赋值给成员变量
    private UserService userService;

    @PostMapping("/login")
//接收POST 请求，访问路径为 /login 的接口，映射到当前方法。

    public ResultBean login(@RequestBody User user, HttpSession session) {
        //接收前端 JSON 格式请求体，自动把 JSON 反序列化为 User 对象
        //SpringMVC 自动注入当前用户会话对象，同一个浏览器多次请求共用同一个 Session
        User loginUser = userService.login(user.getLoginId(), user.getPassword());
        //这里就在 LoginController 里调用 Service 方法
        if (loginUser != null) {
            loginUser.setPassword(null);
            session.setAttribute("userInfo", loginUser);
            //setPassword(null)：把该用户对象的密码属性置为空
            //setAttribute(key, value)：往 Session 中存入数据
            return ResultBean.ok()
                    .message("登录成功")
                    .data("user", loginUser)
                    .data("userType", loginUser.getUserType());
        }
        return ResultBean.error().message("账号或密码错误");
    }

    @PostMapping("/register")
    //@RequestMapping(value = "/login",
    // method = RequestMethod.POST)
    public ResultBean register(@RequestBody User user) {
        if (userService.findByLoginId(user.getLoginId()) != null) {
            return ResultBean.error().message("账号已存在");
        }
        user.setUserType(0);
        user.setStatus(0);
        if (userService.add(user)) {
            return ResultBean.ok().message("注册成功");
        }
        return ResultBean.error().message("注册失败");
    }

    @PostMapping("/logout")
    public ResultBean logout(HttpSession session) {
        session.removeAttribute("userInfo");
        return ResultBean.ok().message("退出成功");
    }
}

//1.项目启动时，Spring 扫描带@Service的UserServiceImpl，提前创建它的对象存入 IOC 容器备用；
//2.前端发起请求，请求进入标注@RestController的LoginController；
//3.Controller 中@Autowired private UserService userService自动从 IOC 容器取出UserServiceImpl实例；
//4.在 Controller 方法内执行userService.方法名()，进入UserServiceImpl对应的业务方法；
//5.UserServiceImpl中@Autowired private UserMapper userMapper，仅作用是获取 MyBatis 生成的 Mapper 代理对象，此时不会执行 SQL；
//6.只有代码执行到userMapper.方法名()时，MyBatis 才会根据接口全类名、方法名匹配UserMapper.xml中对应 SQL，发送语句操作数据库；
//7.数据库把查询 / 修改结果逐层向上回传：XML→Mapper→Service→Controller；
//8.Controller 组装自定义ResultBean对象作为方法返回值，
// 依托@RestController内部包含的@ResponseBody注解，自动把ResultBean对象序列化为 JSON 字符串，响应返回给前端。