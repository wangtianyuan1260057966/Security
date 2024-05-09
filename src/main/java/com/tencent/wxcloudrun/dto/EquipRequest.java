package com.tencent.wxcloudrun.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EquipRequest {

  private String equipArea;

  private String equipName;

  private String equipModel;

  private String equipNo;

  private String equipType;

  private String equipState;

  private String equipOwner;

  private String equipSort;

  private String userId;
}
