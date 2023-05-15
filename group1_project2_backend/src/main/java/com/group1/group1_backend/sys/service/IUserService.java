package com.group1.group1_backend.sys.service;

import com.group1.group1_backend.sys.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author linqingchuan
 * @since 2023-03-16
 */
public interface IUserService extends IService<User> {

    Map<String, Object> login(User user);

//    Map<String, Object> updateUserInfo(String token, User userInfo);

    Map<String, Object> updateRedisUserInfo(String token, User userInfo);

    Map<String, Object> updatePassword(String token, String username, String oldPassword, String newPassword);

    Map<String, Object> getUserInfo(String token);

    void logout(String token);
}
