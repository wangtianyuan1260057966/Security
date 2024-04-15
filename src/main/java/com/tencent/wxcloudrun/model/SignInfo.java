package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SignInfo implements Serializable {
    private String department;

    private String company;

    private String userName;

    private String userJob;

    private String userSign;

    private  LocalDateTime signTime;

    private String openId;
    private String id;
}
