package com.tencent.wxcloudrun.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EquipTypeMapper {
    List<String> getType();

}
