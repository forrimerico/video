package com.imooc.controller;

import com.imooc.service.impl.BgmServiceImpl;
import com.imooc.utils.IMoocJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "BGM相关业务的接口", tags = {"BGM相关业务的controller"})
@RequestMapping("/bgm")
public class BgmController {

    @Autowired
    BgmServiceImpl bgmService;

    @ApiOperation(value = "获取背景音乐列表接口", notes = "获取背景音乐列表接口")
    @PostMapping("list")
    public IMoocJSONResult list()
    {
        return IMoocJSONResult.ok(bgmService.quertBgmList());
    }
}
