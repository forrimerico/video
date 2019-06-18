package com.imooc.service.impl;

import com.imooc.mapper.BgmMapper;
import com.imooc.mapper.VideosMapper;
import com.imooc.pojo.Bgm;
import com.imooc.pojo.Users;
import com.imooc.pojo.Videos;
import com.imooc.service.BgmService;
import com.imooc.service.VideoService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    VideosMapper videosMapper;

    @Autowired
    Sid sid;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String saveVideo(Videos videos) {
        String id = sid.nextShort();
        videos.setId(id);
        videosMapper.insertSelective(videos);

        return id;
    }

    @Override
    public Videos queryVideoById(String id) {
        return videosMapper.selectByPrimaryKey(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateVideo(Videos videos) {
        Example userExample = new Example(Videos.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id", videos.getId());
        videosMapper.updateByExampleSelective(videos, userExample);
    }
}
