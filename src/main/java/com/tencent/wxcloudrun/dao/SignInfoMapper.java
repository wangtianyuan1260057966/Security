package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.Counter;
import com.tencent.wxcloudrun.model.SignInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface SignInfoMapper {
    SignInfo getId(@Param("openId") String openId, @Param("department") String dept, @Param("signTime") Date signTime);

    void upsertCount(Counter counter);

    void clearCount(@Param("id") Integer id);
}
