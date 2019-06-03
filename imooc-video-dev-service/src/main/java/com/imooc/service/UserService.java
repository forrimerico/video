package com.imooc.service;

import com.imooc.pojo.Users;
import org.springframework.stereotype.Service;

public interface UserService {

    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    public boolean queryUserNameIsExist(String username);

    /**
     * 保存用户对象
     * @param user
     */
    public void saveUser(Users user);
}
