package com.imooc.service;


import com.imooc.pojo.Bgm;

import java.util.List;

public interface BgmService {

    /**
     * 查询背景音乐
     * @return
     */
    List<Bgm> queryBgmList();

    Bgm queryBgm(String id);

}
