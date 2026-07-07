package com.lhs.interceptor;

import com.lhs.common.ResultBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 预检请求放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String uri = request.getRequestURI();

        // ========== 获取登录用户信息 ==========
        Object userInfoObj = request.getSession().getAttribute("userInfo");
        if (userInfoObj == null) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(ResultBean.error().message("请先登录")));
            return false;
        }

        // 解析用户信息（需要导入 User 类）
        // 这里假设你有一个 User 类，如果没有就用 Map 方式获取
        com.lhs.model.User user = (com.lhs.model.User) userInfoObj;
        Integer userType = user.getUserType(); // 0=顾客 1=操作员 2=管理员

        // ========== 权限校验 ==========

        // 1. 后台所有接口：操作员(1) 和 管理员(2) 可以访问
        if (uri.startsWith("/api/admin/")) {
            if (userType != 1 && userType != 2) {
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(objectMapper.writeValueAsString(ResultBean.error().message("无权限访问")));
                return false;
            }
        }

        // 2. 供应商管理接口：仅管理员(2) 可以访问
        if (uri.startsWith("/api/admin/supplier")) {
            if (userType != 2) {
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(objectMapper.writeValueAsString(ResultBean.error().message("无权限访问，仅管理员可操作供应商")));
                return false;
            }
        }

        // 3. 账号管理接口：仅管理员(2) 可以访问
        if (uri.startsWith("/api/admin/user")) {
            if (userType != 2) {
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(objectMapper.writeValueAsString(ResultBean.error().message("无权限访问，仅管理员可管理账号")));
                return false;
            }
        }

        // 4. 库存日志接口：仅管理员(2) 可以访问
        if (uri.startsWith("/api/admin/stocklog")) {
            if (userType != 2) {
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(objectMapper.writeValueAsString(ResultBean.error().message("无权限访问，仅管理员可查看库存日志")));
                return false;
            }
        }

        // ========== 放行 ==========
        return true;
    }
}