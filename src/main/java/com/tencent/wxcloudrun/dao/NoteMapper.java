package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.Counter;
import com.tencent.wxcloudrun.model.Note;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface NoteMapper {

  Note getTemp(@Param("dept") String dept);

  Note getPreview(@Param("dept") String dept);

  void updateNote(@Param("note") String note,
                  @Param("otherNote") String otherNote,@Param("changeTime") Date changeTime,
                  @Param("openId") String openid,@Param("dept") String dept);

  void insertNote(@Param("id") String id,@Param("note") String note,
                  @Param("otherNote") String otherNote,@Param("dept") String dept,@Param("changeTime") Date changeTime,@Param("openId") String openid);
}
