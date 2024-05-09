package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;


@Data
public class OrgUser implements Serializable {
    private String userId;
    private String userName;
    private String departmentId;
}
