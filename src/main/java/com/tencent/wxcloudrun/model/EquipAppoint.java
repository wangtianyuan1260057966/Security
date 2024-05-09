package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class EquipAppoint implements Serializable {
    private String subscriber;
    private String customerName;
    private Date appointTime;
    private Date beginTime;
    private Date endTime;
    private String isCancel;
    private String equipArea;
    private String equipNo;
    private String equipOwner;
    private String userId;
    private String id;
}
