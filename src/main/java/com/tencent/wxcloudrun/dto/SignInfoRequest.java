package com.tencent.wxcloudrun.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class SignInfoRequest {


  private String openid;
  private String department;
  private String code;
  private String userSign;
  private String company;
  private String userName;
  private String userJob;
  private String signTime;
  private String endTime;
  private String id;

}
