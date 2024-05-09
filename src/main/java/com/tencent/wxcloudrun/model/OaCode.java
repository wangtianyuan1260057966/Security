package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class OaCode implements Serializable {
    private String oaCode;
    private String openId;
    private String id;
    private String ownerLab;
}
