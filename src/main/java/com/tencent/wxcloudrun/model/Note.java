package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Note implements Serializable {

  private String id;

  private String dept;

  private String otherNote;

  private String note;

  private Date changeTime;
}
