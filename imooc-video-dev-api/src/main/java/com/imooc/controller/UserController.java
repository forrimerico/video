package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.VO.UsersVO;
import com.imooc.service.UserService;
import com.imooc.utils.IMoocJSONResult;
import com.imooc.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.nio.ch.IOUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RestController
@Api(value = "用户相关业务的接口", tags = {"用户相关业务的controller"})
@RequestMapping("/user")
public class UserController extends BasicController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户上传头像接口", notes = "用户注销的接口")
    @ApiImplicitParam(name="userId", value="用户Id", required = true, dataType = "String", paramType = "query")
    @PostMapping("/uploadFace")
    public IMoocJSONResult uploadFace(String userId, @RequestParam("file") MultipartFile[] files) {
        if (StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("用户ID不能为空");
        }

        String fileSpace = "E:\\dev\\product\\imoocvideodev\\imooc_resources";

        String uploadPathDB = "/" + userId + "/face";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try{
            if (files != null && files.length > 0) {
                String filename = files[0].getOriginalFilename();
                if (StringUtils.isNotBlank(filename)) {
                    String finalPath = fileSpace + uploadPathDB + "/" + filename;
                    uploadPathDB += ("/" + filename);
                    File outFile = new File(finalPath);

                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = files[0].getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);

                }
            } else {
                return IMoocJSONResult.errorMsg("上传失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return IMoocJSONResult.errorMsg("上传失败！");
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return IMoocJSONResult.errorMsg("上传失败！");
                }
            }
        }
        Users user = new Users();
        user.setId(userId);
        user.setFaceImage(uploadPathDB);
        userService.updateUserInfo(user);

        return IMoocJSONResult.ok(uploadPathDB);
    }

    @ApiOperation(value = "用户查询接口", notes = "用户查询的接口")
    @ApiImplicitParam(name="userId", value="用户Id", required = true, dataType = "String", paramType = "query")
    @PostMapping("/query")
    public IMoocJSONResult query(String userId)
    {
        if (StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("用户ID不能为空");
        }

        Users user = userService.queryUserInfo(userId);
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user, usersVO);

        return IMoocJSONResult.ok(usersVO);
    }
}
