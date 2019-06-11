package com.imooc.service;

import com.imooc.pojo.Users;
import org.springframework.stereotype.Service;

public interface UserService {

    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    boolean queryUserNameIsExist(String username);

    /**
     * 保存用户对象
     * @param user
     */
    void saveUser(Users user);

    /**
     * 判断密码是否正确
     * @param user
     * @return
     */
    Users queryPasswordRight(Users user) throws Exception;

    /**
     * 更新用户信息
     * @param users
     */
    void updateUserInfo(Users users);

    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    Users queryUserInfo(String userId);
}
