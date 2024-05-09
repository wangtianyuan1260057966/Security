package com.tencent.wxcloudrun.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EquipAppointRequest {

  private String subscriber;
  private String customerName;
  private String appointTime;
  private String beginTime;
  private String endTime;
  private String isCancel;
  private String equipArea;
  private String equipNo;
  private String equipOwner;
  private String userId;
  private String id;
}
