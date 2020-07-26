package com.devops.common.enums;

/**
 * @author yangge
 * @version 1.0.0
 * @date 2020/7/26 22:39
 */
public enum BuildStatusEnum {

    SUCCESS("1", "成功"),
    FAILED("2", "失败");

    private String code;

    private String name;

    BuildStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static BuildStatusEnum get(String code) {
        BuildStatusEnum[] values = values();
        for (BuildStatusEnum value : values) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
