package com.lhs.controller;

import com.lhs.common.PageResult;
import com.lhs.common.ResultBean;
import com.lhs.model.Order;
import com.lhs.model.User;
import com.lhs.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/admin/list")
    public ResultBean adminList(@RequestParam(defaultValue = "1") int pageNum,
                                @RequestParam(defaultValue = "10") int pageSize,
                                @RequestParam(required = false) Integer orderStatus,
                                @RequestParam(required = false) String startTime,
                                @RequestParam(required = false) String endTime) {
        PageResult<Order> pageResult = orderService.findPage(pageNum, pageSize, orderStatus, startTime, endTime);
        return ResultBean.ok().data(pageResult);
    }

    @GetMapping("/my-list")
    public ResultBean myList(HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return ResultBean.error().message("请先登录");
        }
        return ResultBean.ok().data(orderService.findByUserId(user.getUserId()));
    }

    @GetMapping("/{id}")
    public ResultBean getById(@PathVariable Long id) {
        Order order = orderService.findById(id);
        if (order != null) {
            return ResultBean.ok().data(order);
        }
        return ResultBean.error().message("订单不存在");
    }

    @PostMapping("/add")
    public ResultBean add(@RequestBody Order order, HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return ResultBean.error().message("请先登录");
        }
        order.setUserId(user.getUserId());
        if (orderService.add(order)) {
            return ResultBean.ok().message("下单成功");
        }
        return ResultBean.error().message("下单失败");
    }

    @PutMapping("/status")
    public ResultBean updateStatus(@RequestParam Long orderId, @RequestParam Integer orderStatus) {
        if (orderService.updateStatus(orderId, orderStatus)) {
            return ResultBean.ok().message("状态更新成功");
        }
        return ResultBean.error().message("状态更新失败");
    }
}