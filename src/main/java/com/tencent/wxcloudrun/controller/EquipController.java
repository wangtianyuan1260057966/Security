package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.EquipAppointRequest;
import com.tencent.wxcloudrun.dto.EquipRequest;
import com.tencent.wxcloudrun.service.EquipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EquipController {

    final EquipService equipService;
    final Logger logger;

    public EquipController(@Autowired EquipService equipService) {
        this.equipService = equipService;
        this.logger = LoggerFactory.getLogger(CounterController.class);
    }
    @PostMapping(value="/api/search/area")
    public ApiResponse getArea() {
        return ApiResponse.ok(equipService.getArea());

    }
    @PostMapping(value="/api/search/type")
    public ApiResponse getType() {
        return ApiResponse.ok(equipService.getType());

    }

    //获取设备列表，查询结果
    @PostMapping(value="/api/equipment/list")
    public ApiResponse getSearchResult(@RequestBody EquipRequest equipRequest) {
        return equipService.getSearchResult(equipRequest);

    }

    //设备预约
    @PostMapping(value="/api/appoint")
    public ApiResponse appointmentEquip(@RequestBody EquipAppointRequest equipAppointRequest) {
        return equipService.appointEquip(equipAppointRequest);

    }

//    //获取所有大区
    @PostMapping(value="/api/region")
    public ApiResponse getRegion(@RequestParam(value = "userId") String userId) {
        return equipService.getRegion(userId);

    }
//
//    //绑定默认试验室
//    @PostMapping(value="/api/default/department")
//    public ApiResponse setDefaultDepartment( String sid, String ownerLab) {
//
//        return equipService.setDefaultDepartment(ctx, sid, ownerLab);
//
//    }
//
//
//
    //获取某个试验室下能用的设备
    @PostMapping(value="/api/equip")
    public ApiResponse getEquipList(@RequestParam(value = "dept") String dept) {
        return equipService.getEquipList(dept);
    }

    //通过设备编号获取设备基本信息
    @PostMapping(value="/api/equip/info")
    public ApiResponse getEquipInfo(@RequestParam(value = "equipNo") String equipNo) {
        return equipService.getEquipInfo(equipNo);

    }
//
//    //获取我的预约
//    @PostMapping(value="/api/my/appoint")
//    public ApiResponse getMyAppointment( String sid, String state) {
//
//        return equipService.getMyAppointment(ctx, state);
//
//    }
//
//
//    //取消预约
//    @PostMapping(value="/api//appoint/cancel")
//    public ApiResponse cancelAppointment( String sid, String bindid) {
//
//        return equipService.cancelAppointment(ctx, bindid);
//
//    }
//
//    //删除预约
//    @PostMapping(value="/api/appoint/delete")
//    public ApiResponse deleteAppointment( String sid, String bindid) {
//
//        return equipService.deleteAppointment(ctx, bindid);
//
//    }
//
//    //获取用户权限和个人基本信息
//    @PostMapping(value="/api/permission")
//    public ApiResponse getUserPermission( String sid) {
//
//        return equipService.getUserPermission(ctx);
//    }
//
//    //设置用户权限
//    @PostMapping(value="/api/set/permissions")
//    public ApiResponse setUserPermissions( String sid, String users) {
//
//        return equipService.setUserPermissions(ctx, users);
//    }
//
//    //获取全部的用户权限列表
//    @PostMapping(value="/api/user/permissions")
//    public ApiResponse userPermissions( String sid, String region) {
//
//        return equipService.userPermissions(ctx, region);
//    }
//
//    //更改设备状态
//    @PostMapping(value="/api/equip/state")
//    public ApiResponse setEquipState( String sid, String equipNo, String state) {
//
//        return equipService.setEquipState(ctx, equipNo, state);
//    }
//
//    //获取预约的最后日期
//    @PostMapping(value="/api/last/date")
//    public ApiResponse getLastDate( String sid, String ownerLab) {
//
//        return equipService.getLastDate(ctx, ownerLab);
//    }
//
//    //获取预约报表
//    @PostMapping(value="/api/appoint/report")
//    public ApiResponse getAppointmentReport( String sid, String lastDate, String onlyAppoint, String ownerLab, String startDate) {
//
//        return equipService.getAppointmentReport(ctx, lastDate, onlyAppoint, ownerLab, startDate);
//    }
//
//    //获取当前日期具体预约详情
//    @PostMapping(value="/api/appoint/report/info")
//    public ApiResponse getAppointmentReportInfo( String sid, String date, String equipNo) {
//
//        return equipService.getAppointmentReportInfo(ctx, date, equipNo);
//    }
//
//    //下载报表
//    @PostMapping(value="/api/appoint/report/download")
//    public String exportExcel( String sid, String lastDate, String ownerLab) {
//
//        return equipService.exportExcel(ctx, lastDate, ownerLab);
//    }
//
//    //pc端cmd接口
//    //获取角色信息
//    @PostMapping(value="com.awspass.user.apps.mobile.equipment.appointment.role")
//    public boolean isSupperAdmin( String sid, String lastDate, String ownerLab) {
//
//        return equipService.isSupperAdmin(ctx);
//    }
//
//    //列表打开预约
//    @PostMapping(value="com.awspass.user.apps.mobile.equipment.appoint")
//    public String appointEquip( String equipNo) {
//
//        return equipService.appointEquip(ctx, equipNo);
//    }
//
//    //根据角色返回试验室列表
//    @PostMapping(value="com.awspass.user.apps.mobile.equipment.get.role.department")
//    public ApiResponse getRoleDepartment( String sid) {
//
//        return equipService.getRoleDepartment(ctx, sid);
//    }
//
//    //根据sid获取当前人的大区
//    @PostMapping(value="com.awspass.user.apps.mobile.equipment.get.my.depart")
//    public ApiResponse getMyDepart( String sid) {
//
//        return equipService.getMyDepart(ctx, sid);
//    }
//
//    //获取统计数据
//    @PostMapping(value="com.awspass.user.apps.mobile.equipment.get.total.data")
//    public ApiResponse getTotalData( String sid, String ownerLab) {
//
//        return equipService.getTotalData(ctx, sid,ownerLab);
//    }

}
