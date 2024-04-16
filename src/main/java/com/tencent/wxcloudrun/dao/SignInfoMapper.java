package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.Counter;
import com.tencent.wxcloudrun.model.SignInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface SignInfoMapper {
    SignInfo getId(@Param("openId") String openId, @Param("department") String dept, @Param("signTime") Date signTime);

    SignInfo getIds(@Param("openId") String openId, @Param("department") String dept);

    int updateSign(@Param("company") String company,@Param("userName") String userName,@Param("userJob") String userJob,
                   @Param("userSign") String userSign, @Param("signTime") Date signTime,@Param("openId") String openId, @Param("department") String dept);
    int createSign(@Param("id") String id,@Param("company") String company,@Param("userName") String userName,@Param("userJob") String userJob,
                        @Param("userSign") String userSign, @Param("signTime") Date signTime,@Param("openId") String openId, @Param("department") String dept);

}
