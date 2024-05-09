package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.EquipInfo;
import com.tencent.wxcloudrun.model.OaCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EquipInfoMapper {
    List<EquipInfo> getInfo(@Param("equipOwner") String equipOwner, @Param("equipArea") String equipArea,
                            @Param("equipType") String equipType, @Param("equipNo") String equipNo);

    List<EquipInfo> getEquip(@Param("equipNo") String equipNo);

    List<EquipInfo> getDeptEquip(@Param("dept") String dept);
}
