package com.imooc.controller;

import com.imooc.service.impl.BgmServiceImpl;
import com.imooc.utils.IMoocJSONResult;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@Api(value = "视频相关业务的接口", tags = {"视频相关业务的controller"})
@RequestMapping("/video")
public class VideoController {

    @Autowired
    BgmServiceImpl bgmService;


    @ApiOperation(value = "上传视频接口", notes = "上传视频接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId", value="用户Id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name="bgmId", value="背景音乐Id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name="videoSecond", value="背景时长", required = true, dataType = "Double", paramType = "query"),
            @ApiImplicitParam(name="videoWidth", value="视频宽度", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name="videoHeight", value="视频高度", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name="videoDesc", value="视频描述", required = false, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "upload", headers = "content-type=multipart/form-data")
    public IMoocJSONResult upload(String userId,
                                  String bgmId,
                                  Double videoSecond,
                                  Integer videoWidth,
                                  Integer videoHeight,
                                  String videoDesc,
                                  @ApiParam(value = "短视频", required = true)
                                  MultipartFile file) {
        if (StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("用户ID不能为空");
        }

        String fileSpace = "E:\\dev\\product\\imoocvideodev\\imooc_resources";

        String uploadPathDB = "/" + userId + "/video";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try{
            if (file != null) {
                String filename = file.getOriginalFilename();
                if (StringUtils.isNotBlank(filename)) {
                    String finalPath = fileSpace + uploadPathDB + "/" + filename;
                    uploadPathDB += ("/" + filename);
                    File outFile = new File(finalPath);

                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
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
//        Users user = new Users();
//        user.setId(userId);
//        user.setFaceImage(uploadPathDB);
//        userService.updateUserInfo(user);

        return IMoocJSONResult.ok(uploadPathDB);
    }
}
