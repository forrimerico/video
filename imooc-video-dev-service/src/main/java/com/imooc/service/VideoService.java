package com.imooc.service;

import com.imooc.pojo.SearchRecords;
import com.imooc.pojo.VO.VideosVO;
import com.imooc.pojo.Videos;
import com.imooc.utils.PagedResult;

import java.util.List;

public interface VideoService {

    String saveVideo(Videos videos);

    Videos queryVideoById(String id);

    void updateVideo(Videos videos);

    PagedResult getAllVideos(Videos videos, Integer isSaveRecord, Integer page, Integer pageSize);

    List<String> getSearchRecords();

    void addLike(String userId, String videoId, String videoCreaterId);

    void reduceLike(String userId, String videoId, String videoCreaterId);

}
