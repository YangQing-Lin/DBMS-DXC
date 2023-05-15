package com.group1.group1_backend.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.group1.group1_backend.comm.vo.Result;
import com.group1.group1_backend.sys.entity.User;
import com.group1.group1_backend.sys.service.IUserService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author linqingchuan
 * @since 2023-03-16
 */
@RestController
@RequestMapping("/user")
//@CrossOrigin  // 跨域处理
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public Result<?> addUser(@RequestBody User user) {
//        加密用户密码
        if (user.getPassword() != null) {
            user.setPassword((passwordEncoder.encode(user.getPassword())));
        }
        userService.save(user);
        return Result.success("新增用户成功");
    }

    @GetMapping("/all")
    public Result<List<User>> getAllUser() {
        List<User> list = userService.list();
        list.forEach(System.out::println);
        return Result.success("查询成功", list);
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user) {
        Map<String, Object> data = userService.login(user);
        if(data != null){
            return Result.success(data);
        }
        return Result.fail(20002,"用户名或密码错误");
    }

    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo(@RequestParam("token") String token) {
//        根据token获取用户信息，从redis里获取
        Map<String, Object> data = userService.getUserInfo(token);
        System.out.printf("in user controller", data);
        if (data != null) {
            return Result.success(data);
        }
        return Result.fail(20003, "登录信息无效，请重新登陆");
    }

    @PostMapping("/logout")
    public Result<?> logout(@RequestHeader("X-Token") String token) {
        userService.logout(token);
        return Result.success();
    }

    @GetMapping("/list")

    public Result<Map<String, Object>> getUserList(@RequestParam(value = "username", required = false) String username,
                                                   @RequestParam(value = "phone", required = false) String phone,
                                                   @RequestParam(value = "startTime", required = false) String startTime,
                                                   @RequestParam(value = "endTime", required = false) String endTime,
                                                   @RequestParam(value = "createTime",required = false) Date createTime,
                                                   @RequestParam(value = "pageNo") Long pageNo,
                                                   @RequestParam(value = "pageSize") Long pageSize) {

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(username), User::getUsername, username);
        wrapper.eq(StringUtils.hasLength(phone), User::getPhone, phone);
        wrapper.between(startTime != null && endTime != null, User::getCreateTime, startTime, endTime);

        Page<User> page = new Page<>(pageNo, pageSize);
        userService.page(page, wrapper);
        Map<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("rows", page.getRecords());
        return Result.success(data);

    }

    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable("id") Integer id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    @PutMapping
    public Result<?> updateUser(@RequestParam("token") String token, @RequestBody User user) {
//        login(user);  // 更改用户时后端需要重新登录，将信息更新到Redis
//        加密用户密码
        user.setPassword(null);
        System.out.println("update user: " + token + user);
        userService.updateById(user);  // 如果传入的字段为空，那么就不会修改，这个api中没有提供密码的修改功能
        userService.updateRedisUserInfo(token, user);
        return Result.success("修改用户成功");
    }

    @PostMapping("/password")
    public Result<Map<String, Object>> updatePassword(@RequestParam("token") String token,
                                                      @RequestParam("username") String username,
                                                      @RequestParam("oldPassword") String oldPassword,
                                                      @RequestParam("newPassword") String newPassword) {
        System.out.println("update password: " + token + " " + username + " " + oldPassword + " " + newPassword);
        Map<String, Object> data = userService.updatePassword(token, username, oldPassword, newPassword);
        if(data != null){
            userService.updateById((User) data.get("loginUser"));
            return Result.success(data);
        }
        return Result.fail(20004,"更改密码失败");
    }

    @DeleteMapping("/{id}")
    public Result<User> deleteUserById(@PathVariable("id") Integer id) {
        userService.removeById(id);
        return Result.success("删除用户成功");
    }

}