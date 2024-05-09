package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.OaCode;
import com.tencent.wxcloudrun.model.SignInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginInfoMapper {
    OaCode getLab(@Param("openId") String openId);
    OaCode getUser(@Param("openId") String openId);
    OaCode getOpenId(@Param("oaCode") String oaCode);

   int createLogin(@Param("id") String id,@Param("openId") String openId, @Param("oaCode") String oaCode);

   int setLab(@Param("openId") String openId, @Param("ownerLab") String ownerLab);
}
