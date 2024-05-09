package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.EquipAppoint;
import com.tencent.wxcloudrun.model.EquipInfo;
import com.tencent.wxcloudrun.model.OaCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EquipAppointMapper {
    List<EquipAppoint> getAppoint(@Param("equipNo") String equipNo, @Param("endTime") String endTime);
    void appointEquip(@Param("id") String id,@Param("equipNo") String equipNo,@Param("subscriber") String subscriber,@Param("endTime") String endTime,
                      @Param("customerName") String customerName,@Param("appointTime") String appointTime,@Param("beginTime") String beginTime,
                      @Param("isCancel") String isCancel,@Param("userId") String userId,@Param("equipArea") String equipArea,@Param("equipName") String equipName,@Param("equipOwner") String equipOwner);

}
