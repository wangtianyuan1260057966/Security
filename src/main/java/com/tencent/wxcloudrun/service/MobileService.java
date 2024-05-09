package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.NoteRequest;
import com.tencent.wxcloudrun.dto.SignInfoRequest;

public interface MobileService {
    ApiResponse firstCheck(String dept, String code) ;
    ApiResponse save(SignInfoRequest  data);
    ApiResponse getNote(String dept);
    ApiResponse loginCheck(String code) ;
    ApiResponse setLab(String code,String dept) ;

    ApiResponse signIn(String code, String oaCode) ;

    ApiResponse getOtherNote(String dept);
    ApiResponse setNote(NoteRequest data);

    ApiResponse getAllLab() ;

    ApiResponse getInfo(SignInfoRequest data);

    ApiResponse getExport(String data, String dept);
}
