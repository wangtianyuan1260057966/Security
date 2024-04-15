package com.tencent.wxcloudrun.controller;


import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.CounterRequest;
import com.tencent.wxcloudrun.dto.SignInfoRequest;
import com.tencent.wxcloudrun.service.MobileService;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileController {

    final MobileService mobileService;
    final Logger logger;

    public MobileController(MobileService mobileService, Logger logger) {
        this.mobileService = mobileService;
        this.logger = logger;
    }


    @PostMapping(value = "/api/check")
    boolean firstCheck(@RequestBody SignInfoRequest request) {
        logger.info("111");
        return mobileService.firstCheck(request.getDepartment(),request.getCode());
    }
    @PostMapping(value = "/api/save")
    ApiResponse save(@RequestBody String data) {

        return mobileService.save(data);
    }

    @PostMapping(value = "/api/note")
    ApiResponse getNote(@RequestBody String dept) {

        return mobileService.getNote(dept);
    }

    @PostMapping(value = "/api/security")
    ApiResponse getDept(@RequestBody String dept) {
        return ApiResponse.ok(dept);
    }
}