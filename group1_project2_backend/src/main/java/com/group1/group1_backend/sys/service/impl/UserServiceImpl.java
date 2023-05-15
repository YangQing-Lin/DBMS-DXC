package com.group1.group1_backend.sys.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.group1.group1_backend.sys.entity.User;
import com.group1.group1_backend.sys.mapper.UserMapper;
import com.group1.group1_backend.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author linqingchuan
 * @since 2023-03-16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 登录逻辑
     * @param user
     * @return
     */
    @Override
    public Map<String, Object> login(User user) {
//        根据用户名和密码查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        User loginUser = this.baseMapper.selectOne(wrapper);  // 从数据库中获取用户名相同的用户的信息

//        结果不为空，并且密码和传入密码匹配，则生成一个token，并且将用户信息存入redis
        if (loginUser != null && passwordEncoder.matches(user.getPassword(), loginUser.getPassword())) {
//            if (loginUser != null && loginUser.getPassword().equals(user.getPassword())) {
//            暂时用UUID，终极方案是jwt
            String key = "user:" + UUID.randomUUID();

//            存入redis
            loginUser.setPassword(null);
            redisTemplate.opsForValue().set(key, loginUser,30, TimeUnit.MINUTES);
//            返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("token", key);

            return data;
        }

        return null;
    }

    @Override
    public Map<String, Object> updateRedisUserInfo(String token, User userInfo) {
        redisTemplate.opsForValue().set(token, userInfo,10, TimeUnit.MINUTES);
        return null;
    }

    @Override
    public Map<String, Object> updatePassword(String token, String username, String oldPassword, String newPassword) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User loginUser = this.baseMapper.selectOne(wrapper);  // 从数据库中获取用户名相同的用户的信息
        System.out.println("login user: " + loginUser);

        if (loginUser != null && passwordEncoder.matches(oldPassword, loginUser.getPassword())) {
            redisTemplate.delete(token);  // 成功修改密码时需要删除之前的登录信息，重新登录
            loginUser.setPassword(passwordEncoder.encode(newPassword));
            System.out.println("save login user: " + loginUser);
//            save(loginUser);
            System.out.println("save new password success");
            Map<String, Object> data = new HashMap<>();
            data.put("loginUser", loginUser);
            data.put("access", "update password SUCCESS");

            return data;
        }
        return null;
    }

//    @Override
//    public Map<String, Object> login(User user) {
////        根据用户名和密码查询
//        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(User::getUsername, user.getUsername());
//        wrapper.eq(User::getPassword, user.getPassword());
//        User loginUser = this.baseMapper.selectOne(wrapper);
//
////        结果不为空，则生成一个token，并且将用户信息存入redis
//        if (loginUser != null) {
////            暂时用UUID，终极方案是jwt
//            String key = "user:" + UUID.randomUUID();
//
////            存入redis
//            loginUser.setPassword(null);
//            redisTemplate.opsForValue().set(key, loginUser,10, TimeUnit.MINUTES);
//
////            返回数据
//            Map<String, Object> data = new HashMap<>();
//            data.put("token", key);
//
//            return data;
//        }
//
//        return null;
//    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        System.out.println("in get user info");
//        根据token获取用户信息，从redis里获取
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
//            将传入的字符串反序列化为想要的类型
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            Map<String, Object> data = new HashMap<>();
            data.put("name", loginUser.getName());
            data.put("username", loginUser.getUsername());
            data.put("url", loginUser.getUrl());
            data.put("phone", loginUser.getPhone());
            data.put("type", loginUser.getType());
            data.put("email", loginUser.getEmail());
            data.put("createTime", loginUser.getCreateTime());
            data.put("id", loginUser.getId());
//            角色
            List<String> roleList = this.baseMapper.getRoleNamesByUserId(loginUser.getId());
            data.put("roles", roleList);
            System.out.println("data :" +  data);
            return data;
        }
        return null;
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete(token);
    }
}
