package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.service.UserService;
import com.imooc.utils.IMoocJSONResult;
import com.imooc.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "用户注册登录的接口", tags = {"注册和登录的controller"})
public class RegistLoginController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     * @param user
     * @return
     */
    @ApiOperation(value = "用户注册接口", notes = "用户注册的接口")
    @PostMapping("/regist")
    public IMoocJSONResult regist(@RequestBody Users user) {
        try {
            // 用户名和密码不为空
            if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
                return IMoocJSONResult.errorMsg("用户名和密码为空！");
            }

            // 用户名是否存在
            if (userService.queryUserNameIsExist(user.getUsername())) {
                return IMoocJSONResult.errorMsg("用户名已经存在！");
            }

            // 保存用户信息
            user.setNickname(user.getUsername());
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            user.setFansCounts(0);
            user.setFollowCounts(0);
            userService.saveUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return IMoocJSONResult.errorMsg(e.getMessage());
        }

        return IMoocJSONResult.ok();
    }
}
