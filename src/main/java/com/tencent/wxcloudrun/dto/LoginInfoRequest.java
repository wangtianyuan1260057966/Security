package com.tencent.wxcloudrun.dto;

import lombok.Data;

@Data
public class LoginInfoRequest {


  private String openid;

  private String code;

  private String oaCode;

  private String id;
  private String ownerLab;

}
