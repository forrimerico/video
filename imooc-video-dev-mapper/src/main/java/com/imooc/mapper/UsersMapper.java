package com.imooc.mapper;

import com.imooc.pojo.Users;
import com.imooc.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface UsersMapper extends MyMapper<Users> {

    void addUserLikeCount(@Param("userId") String userId);

    void reduceUserLikeCount(@Param("userId") String userId);
}