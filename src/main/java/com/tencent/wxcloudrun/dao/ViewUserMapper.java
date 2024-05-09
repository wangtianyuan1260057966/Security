package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.OrgDepartment;
import com.tencent.wxcloudrun.model.ViewUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ViewUserMapper {


    String getDeptZone(@Param("userId") String userId);
//   int createLogin(@Param("id") String id,@Param("openId") String openId, @Param("oaCode") String oaCode);

}
