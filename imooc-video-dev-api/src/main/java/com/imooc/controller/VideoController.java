package com.imooc.controller;

import com.imooc.enums.VideoStatusEnums;
import com.imooc.pojo.Bgm;
import com.imooc.pojo.Videos;
import com.imooc.service.impl.BgmServiceImpl;
import com.imooc.service.impl.VideoServiceImpl;
import com.imooc.utils.FfmepgOperrator;
import com.imooc.utils.IMoocJSONResult;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@RestController
@Api(value = "视频相关业务的接口", tags = {"视频相关业务的controller"})
@RequestMapping("/video")
public class VideoController extends BasicController {

    @Autowired
    BgmServiceImpl bgmService;

    @Autowired
    VideoServiceImpl videoService;


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
                                  int videoWidth,
                                  int videoHeight,
                                  String videoDesc,
                                  @ApiParam(value = "短视频", required = true)
                                  MultipartFile file) {
        if (StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("用户ID不能为空");
        }

        String uploadPathDB = "/" + userId + "/video";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        String finalPath = "";
        try{
            if (file != null) {
                String filename = file.getOriginalFilename();
                if (StringUtils.isNotBlank(filename)) {
                    finalPath = FILESPACE + uploadPathDB + "/" + filename;
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

        if (StringUtils.isNotBlank(bgmId)) {
            Bgm bgm = bgmService.queryBgm(bgmId);
            String mp3InputPath = FILESPACE + bgm.getPath();
            FfmepgOperrator ffmepgOperrator = new FfmepgOperrator(FFMEPGPATH);
            uploadPathDB = "/" + userId + "/video/" + UUID.randomUUID().toString() + ".mp4";
            String outputPath = FILESPACE + uploadPathDB;
            System.out.println(finalPath);
            System.out.println(mp3InputPath);
            System.out.println(videoSecond);
            System.out.println(outputPath);
            try {
                ffmepgOperrator.addBgm(finalPath, mp3InputPath, 3.0, outputPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Videos videos = new Videos();
        videos.setAudioId(bgmId);
        videos.setUserId(userId);
//        videos.setVideoSeconds(Float.parseFloat(videoSecond.toString()));
        videos.setVideoDesc(videoDesc);
        videos.setVideoHeight(videoHeight);
        videos.setVideoWidth(videoWidth);
        videos.setVideoPath(uploadPathDB);
        videos.setStatus(VideoStatusEnums.SUCCESS.getValue());
        videos.setCreateTime(new Date());

        return IMoocJSONResult.ok(videoService.saveVideo(videos));
    }

    @ApiOperation(value = "上传视频封面接口", notes = "上传视频封面接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId", value="用户Id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name="videoId", value="videoId", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping(value = "uploadCover", headers = "content-type=multipart/form-data")
    public IMoocJSONResult upload(String userId,
                                  String videoId,
                                  @ApiParam(value = "短视频", required = true)
                                          MultipartFile file) {
        if (StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("用户ID不能为空");
        }

        String uploadPathDB = "/" + userId + "/video/cover";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        String finalPath = "";
        try{
            if (file != null) {
                String filename = file.getOriginalFilename();
                if (StringUtils.isNotBlank(filename)) {
                    finalPath = FILESPACE + uploadPathDB + "/" + filename;
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

        Videos videos = videoService.queryVideoById(videoId);
        videos.setCoverPath(uploadPathDB);
        videoService.updateVideo(videos);

        return IMoocJSONResult.ok();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name="page", value="页码", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name="pageSize", value="一页展示数量", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name="isSaveRecord", value="是否保存热搜", required = false, dataType = "Integer", paramType = "query"),
    })
    @ApiOperation(value = "获取视频列表", notes = "获取视频列表")
    @PostMapping("/showAll")
    public IMoocJSONResult list(@RequestBody Videos videos, Integer isSaveRecord, Integer page, Integer pageSize)
    {
        if (page == null) page = 1;
        if (pageSize == null) pageSize = 10;

        return IMoocJSONResult.ok(videoService.getAllVideos(videos, isSaveRecord, page, pageSize));
    }

    @ApiOperation(value = "获取热搜接口", notes = "获取热搜接口")
    @PostMapping("/hot")
    public IMoocJSONResult hot()
    {
        return IMoocJSONResult.ok(videoService.getSearchRecords());
    }
}
