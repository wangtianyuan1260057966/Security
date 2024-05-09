package com.tencent.wxcloudrun.controller;


import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.CounterRequest;
import com.tencent.wxcloudrun.dto.LoginInfoRequest;
import com.tencent.wxcloudrun.dto.NoteRequest;
import com.tencent.wxcloudrun.dto.SignInfoRequest;
import com.tencent.wxcloudrun.service.MobileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class MobileController {

    final MobileService mobileService;
    final Logger logger;


    public MobileController(MobileService mobileService) {
        this.mobileService = mobileService;
        this.logger = LoggerFactory.getLogger(CounterController.class);;
    }

    //检查是否绑定过
    @PostMapping(value = "/api/login/check")
    public ApiResponse loginCheck(@RequestBody LoginInfoRequest loginInfoRequest) {
        logger.info("222222+"+loginInfoRequest.getCode());
        return mobileService.loginCheck(loginInfoRequest.getCode());
    }

    //绑定默认试验室
    @PostMapping(value = "/api/set/lab")
    public ApiResponse setLab(@RequestBody LoginInfoRequest loginInfoRequest) {
        return mobileService.setLab(loginInfoRequest.getCode(), loginInfoRequest.getOwnerLab());
    }

    //绑定
    @PostMapping(value = "/api/login")
    public ApiResponse signIn(@RequestBody LoginInfoRequest loginInfoRequest) {
        logger.info(loginInfoRequest.getCode());
        return mobileService.signIn(loginInfoRequest.getCode(), loginInfoRequest.getOaCode());
    }

    //获取所有试验室
    @PostMapping(value = "/api/get/lab")
    public ApiResponse getAllLab() {
        return mobileService.getAllLab();
    }
    @PostMapping(value = "/api/check")
    public ApiResponse firstCheck(@RequestBody SignInfoRequest request) {
        logger.info("111");
        return mobileService.firstCheck(request.getDepartment(),request.getCode());
    }
    @PostMapping(value = "/api/save")
    ApiResponse save(@RequestBody SignInfoRequest data) {

        return mobileService.save(data);
    }

    @PostMapping(value = "/api/note")
    ApiResponse getNote(@RequestParam(value = "dept") String dept) {
        return mobileService.getNote(dept);
    }

    @PostMapping(value = "/api/other/note")
    ApiResponse getOtherNote(@RequestParam(value = "dept") String dept) {
        return mobileService.getOtherNote(dept);
    }

    @PostMapping(value = "/api/set/note")
    ApiResponse setNote(@RequestBody NoteRequest data) {
        return mobileService.setNote(data);
    }

    @PostMapping(value = "/api/security")
    ApiResponse getDept(@RequestParam(value = "dept") String dept) {
        return ApiResponse.ok(dept);
    }

    @PostMapping(value = "/api/get/info")
    ApiResponse getInfo(@RequestBody SignInfoRequest data) {
        return mobileService.getInfo(data);
    }

    @PostMapping(value = "/api/get/export")
    ApiResponse getExport(@RequestParam(value = "data") String data,@RequestParam(value = "dept") String dept) {
        return mobileService.getExport(data, dept);
    }
}