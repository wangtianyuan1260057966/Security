package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;


@Data
public class ViewUser implements Serializable {
    private String departmentNo;
    private String departmentName;
    private String departmentId;
    private String outerId;
    private String userId;
    private String userName;

}
