package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.OrgDepartment;
import com.tencent.wxcloudrun.model.OrgUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrgDepartmentMapper {
    List<OrgDepartment> getDept();


    List<OrgDepartment> getRegion(@Param("region") String region);
//   int createLogin(@Param("id") String id,@Param("openId") String openId, @Param("oaCode") String oaCode);

}
