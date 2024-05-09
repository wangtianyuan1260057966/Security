package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.EquipAppointRequest;
import com.tencent.wxcloudrun.dto.EquipRequest;
import com.tencent.wxcloudrun.model.Counter;
import com.tencent.wxcloudrun.model.EquipInfo;

import java.util.List;
import java.util.Optional;

public interface EquipService {

  List<String> getArea();
  List<String> getType();

  ApiResponse getSearchResult(EquipRequest equipRequest);

  ApiResponse appointEquip(EquipAppointRequest equipAppointRequest);

  ApiResponse getRegion(String userId);
  ApiResponse getEquipList(String dept);
  ApiResponse getEquipInfo(String equipNo);
//
//  void getMyAppointment();
//
//  void cancelAppointment();
}
