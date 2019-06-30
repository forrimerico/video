package com.imooc.mapper;

import com.imooc.pojo.VO.VideosVO;
import com.imooc.pojo.Videos;
import com.imooc.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideosMapper extends MyMapper<Videos> {

    List<VideosVO> queryAllVideos(@Param("videoDesc") String videoDesc);

}