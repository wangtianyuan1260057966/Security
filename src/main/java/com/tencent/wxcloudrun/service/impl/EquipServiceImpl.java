package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dao.*;
import com.tencent.wxcloudrun.dto.EquipAppointRequest;
import com.tencent.wxcloudrun.dto.EquipRequest;
import com.tencent.wxcloudrun.model.*;
import com.tencent.wxcloudrun.service.CounterService;
import com.tencent.wxcloudrun.service.EquipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EquipServiceImpl implements EquipService {

  final EquipInfoMapper equipInfoMapper;
  final EquipAreaMapper equipAreaMapper;

  final EquipTypeMapper equipTypeMapper;
  final EquipAppointMapper equipAppointMapper;

  final OrgUserMapper orgUserMapper;
  final OrgDepartmentMapper orgDepartmentMapper;

  final ViewUserMapper viewUserMapper;



  public EquipServiceImpl(@Autowired EquipInfoMapper equipInfoMapper, EquipAreaMapper equipAreaMapper, EquipTypeMapper equipTypeMapper, EquipAppointMapper equipAppointMapper, OrgUserMapper orgUserMapper, OrgDepartmentMapper orgDepartmentMapper, ViewUserMapper viewUserMapper) {
    this.equipInfoMapper = equipInfoMapper;
      this.equipAreaMapper = equipAreaMapper;
      this.equipTypeMapper = equipTypeMapper;
      this.equipAppointMapper = equipAppointMapper;
      this.orgUserMapper = orgUserMapper;
      this.orgDepartmentMapper = orgDepartmentMapper;
      this.viewUserMapper = viewUserMapper;
  }

  @Override
  public List<String> getArea(){
    return equipAreaMapper.getArea();
  }
  @Override
  public List<String> getType(){
    return equipTypeMapper.getType();
  }

  @Override
  public ApiResponse getSearchResult(EquipRequest equipRequest) {
    if (equipRequest.getEquipOwner().isEmpty()) {
      return ApiResponse.error("owner不能为空");
    }
    List<Map<String, Object>> resultList = new ArrayList<>();
    List<EquipInfo> info = equipInfoMapper.getInfo(equipRequest.getEquipOwner(), equipRequest.getEquipArea(),
            equipRequest.getEquipType(), "%" + equipRequest.getEquipNo() + "%");
    for(EquipInfo equipInfo : info) {
      Map<String, Object> equipMap = new HashMap<>();
      String equipNo = equipInfo.getEquipNo();
      equipMap.put("EQUIP_NO", equipNo);
      equipMap.put("EQUIP_NAME",equipInfo.getEquipName());
      equipMap.put("EQUIP_STATE",equipInfo.getEquipState());
      equipMap.put("EQUIP_OWNER",equipInfo.getEquipOwner());
      equipMap.put("EQUIP_MODEL",equipInfo.getEquipModel());
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      String now = dateFormat.format(new Date());
      List<EquipAppoint> appoint = equipAppointMapper.getAppoint(equipNo, now);
      List<Map<String, Object>> list = new ArrayList<>();
      for (EquipAppoint equipAppoint : appoint) {
        Map<String, Object> apprMap = new HashMap<>();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar dd = Calendar.getInstance();
        dd.setTime(new Date());
        dd.add(Calendar.DATE, -1);
        Date date = new Date();
        try {
          date = format.parse( dateFormat.format(dd.getTime())+ " 23:59:59");
        } catch (ParseException e) {
          e.printStackTrace();
        }
        Date endTime = equipAppoint.getEndTime();
        if (endTime != null && endTime.after(date)) {
          String userId = equipAppoint.getUserId();
          apprMap.put("SUBSCRIBER",equipAppoint.getSubscriber());
          apprMap.put("CUSTOMER_NAME",equipAppoint.getCustomerName());
          apprMap.put("BEGIN_TIME",equipAppoint.getBeginTime());
          apprMap.put("END_TIME",equipAppoint.getEndTime());
          apprMap.put("APPOINT_TIME",equipAppoint.getAppointTime());
          apprMap.put("USER_ID",userId);
          if (userId.equals(equipRequest.getUserId())) {
            apprMap.put("IS_ME","true");
          } else {
            apprMap.put("IS_ME","false");
          }
          apprMap.put("ID",equipAppoint.getId());
          list.add(apprMap);
        }
      }
      equipMap.put("APPOINTMENT_INFO",list);
      resultList.add(equipMap);
    }
    return ApiResponse.ok(resultList);
  }

  @Override
  public ApiResponse appointEquip(EquipAppointRequest equipAppointRequest) {
    String uid = equipAppointRequest.getUserId();
    if (equipAppointRequest.getEquipNo().isEmpty()|| equipAppointRequest.getCustomerName().isEmpty()
            || equipAppointRequest.getBeginTime()==null || equipAppointRequest.getEndTime()==null) {

      return ApiResponse.error("参数为空");
    }
    List<EquipInfo> equip = equipInfoMapper.getEquip(equipAppointRequest.getEquipNo());

    if (equip.isEmpty()) {
      return ApiResponse.error("未查询到设备信息");
    }
    String uuid = UUID.randomUUID().toString();
    String sub = orgUserMapper.getUserName(uid);
    DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
    equipAppointMapper.appointEquip(uuid, equipAppointRequest.getEquipNo(), sub,
            equipAppointRequest.getEndTime(),equipAppointRequest.getCustomerName(),fmt.format(new Date()),
            equipAppointRequest.getBeginTime(),"false", uid, equip.get(0).getEquipArea(),equip.get(0).getEquipName(),equip.get(0).getEquipOwner());

    return ApiResponse.ok();
  }

  @Override
  public ApiResponse getRegion(String userId) {
    List<OrgDepartment> region = orgDepartmentMapper.getRegion(viewUserMapper.getDeptZone(userId));
    return ApiResponse.ok(region);
  }
//
//  @Override
//  public void setDefaultDepartment() {
//
//  }
//
  @Override
  public ApiResponse getEquipList(String dept) {
    return ApiResponse.ok(equipInfoMapper.getDeptEquip(dept));
  }

  @Override
  public ApiResponse getEquipInfo(String equipNo) {
    return ApiResponse.ok(equipInfoMapper.getEquip(equipNo));
  }
//
//  @Override
//  public void getMyAppointment() {
//
//  }
//
//  @Override
//  public void cancelAppointment() {
//
//  }


}
