package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.config.ApiResponse;

public interface MobileService {
    boolean firstCheck(String dept, String code) ;
    ApiResponse save(String data);
    ApiResponse getNote(String dept);


}
