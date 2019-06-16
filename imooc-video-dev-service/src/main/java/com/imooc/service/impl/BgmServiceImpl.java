package com.imooc.service.impl;

import com.imooc.mapper.BgmMapper;
import com.imooc.pojo.Bgm;
import com.imooc.service.BgmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BgmServiceImpl implements BgmService {

    @Autowired
    BgmMapper bgmMapper;

    @Override
    public List<Bgm> queryBgmList() {
        return bgmMapper.selectAll();
    }

    @Override
    public Bgm queryBgm(String id) {
        return bgmMapper.selectByPrimaryKey(id);
    }


}
