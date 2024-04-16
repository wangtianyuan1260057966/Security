package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.SignInfoRequest;

public interface MobileService {
    boolean firstCheck(String dept, String code) ;
    ApiResponse save(SignInfoRequest  data);
    ApiResponse getNote(String dept);


}
