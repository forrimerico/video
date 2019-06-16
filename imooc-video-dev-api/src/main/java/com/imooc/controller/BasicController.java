package com.imooc.controller;

import com.imooc.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {

    @Autowired
    public RedisOperator redis;

    public final static String USER_REDIS_SESSION = "user-resdis-session";

    public final static String FFMEPGPATH = "E:\\dev\\ffmpeg-20190614-dd357d7-win64-static\\bin\\ffmpeg.exe";
    public final static String FILESPACE = "E:\\dev\\product\\imoocvideodev\\imooc_resources";
}
