package com.tencent.wxcloudrun.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SignInfoRequest {


  private String openid;
  private String department;
  private String code;
  private LocalDateTime signTime;

}
