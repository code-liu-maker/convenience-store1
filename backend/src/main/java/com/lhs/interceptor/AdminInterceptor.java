package com.lhs.interceptor;

import com.lhs.common.ResultBean;
import com.lhs.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // ========== 添加这段测试放行代码 ==========
        String uri = request.getRequestURI();
        if (uri.contains("/admin/user")) {
            return true;  // 测试时直接放行
        }
        // ========== 测试放行代码结束 ==========

        Object userInfo = request.getSession().getAttribute("userInfo");

        if (userInfo == null) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(ResultBean.error().message("请先登录")));
            return false;
        }

        User user = (User) userInfo;
        if (user.getUserType() != 2) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(ResultBean.error().message("无权限访问")));
            return false;
        }
        return true;
    }
}