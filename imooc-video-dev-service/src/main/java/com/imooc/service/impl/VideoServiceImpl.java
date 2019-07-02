package com.imooc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mapper.*;
import com.imooc.pojo.*;
import com.imooc.pojo.VO.VideosVO;
import com.imooc.service.VideoService;
import com.imooc.utils.PagedResult;
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
    SearchRecordsMapper searchRecordsMapper;

    @Autowired
    UsersMapper usersMapper;

    @Autowired
    UsersLikeVideoMapper usersLikeVideoMapper;

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

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PagedResult getAllVideos(Videos videos,
                                    Integer isSaveRecord, Integer page, Integer pageSize) {

        String videoDesc = videos.getVideoDesc();
        if (isSaveRecord != null && isSaveRecord > 0 ) {
            String id = sid.nextShort();
            SearchRecords searchRecords = new SearchRecords();
            searchRecords.setId(id);
            searchRecords.setContent(videoDesc);
            searchRecordsMapper.insert(searchRecords);
        }
        PageHelper.startPage(page, pageSize);
        List<VideosVO> list = videosMapper.queryAllVideos(videoDesc);

        PageInfo<VideosVO> pageList = new PageInfo<>(list);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRecords(pageList.getTotal());
        pagedResult.setRows(list);

        return pagedResult;
    }

    @Override
    public List<String> getSearchRecords() {
        return searchRecordsMapper.selectHots();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addLike(String userId, String videoId, String videoCreaterId) {
        String uid = sid.nextShort();
        UsersLikeVideos usersLikeVideo = new UsersLikeVideos();
        usersLikeVideo.setUserId(userId);
        usersLikeVideo.setVideoId(videoId);
        usersLikeVideo.setId(uid);
        usersLikeVideoMapper.insert(usersLikeVideo);
        videosMapper.addVideoLikeCount(videoId);
        usersMapper.addUserLikeCount(videoCreaterId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void reduceLike(String userId, String videoId, String videoCreaterId) {
        UsersLikeVideos usersLikeVideo = new UsersLikeVideos();
        usersLikeVideo.setUserId(userId);
        usersLikeVideo.setVideoId(videoId);
        usersLikeVideoMapper.delete(usersLikeVideo);
        videosMapper.reduceVideoLikeCount(videoId);
        usersMapper.reduceUserLikeCount(videoCreaterId);
    }
}
