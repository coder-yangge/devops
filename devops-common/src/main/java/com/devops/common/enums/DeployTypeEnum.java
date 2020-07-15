package com.devops.common.enums;

/**
 * @author yangge
 * @version 1.0.0
 * @title: DeployTypeEnum
 * @date 2020/7/15 22:34
 */
public enum DeployTypeEnum {

    MACHINE(1, "服务器"),
    CLUSTER(2, "集群");

    private Integer code;

    private String name;

    DeployTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static DeployTypeEnum get(Integer code) {
        DeployTypeEnum[] values = values();
        for (DeployTypeEnum value : values) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }}
