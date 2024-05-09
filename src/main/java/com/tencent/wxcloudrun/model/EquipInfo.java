package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class EquipInfo implements Serializable {
    private String equipArea;

    private String equipName;

    private String equipModel;

    private String equipNo;

    private String equipType;

    private String equipState;
    private String equipOwner;
    private String equipSort;
}
