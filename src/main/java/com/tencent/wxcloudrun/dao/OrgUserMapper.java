package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.OaCode;
import com.tencent.wxcloudrun.model.OrgUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrgUserMapper {
    OrgUser getUser(@Param("userId") String userId);

    String getUserName(@Param("userId") String userId);

   int createLogin(@Param("id") String id,@Param("openId") String openId, @Param("oaCode") String oaCode);

}
