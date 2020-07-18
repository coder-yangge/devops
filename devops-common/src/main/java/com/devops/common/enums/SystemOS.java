package com.devops.common.enums;

/**
 * @author yangge
 * @version 1.0.0
 * @title: SystemOS
 * @date 2020/7/18 18:47
 */
public enum SystemOS {

    LINUX("linux", "linux"),
    WINDOWS("windows", "windows");

    private String code;

    private String name;

    SystemOS(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static SystemOS get(String code) {
        SystemOS[] values = values();
        for (SystemOS value : values) {
            if (code.startsWith(value.getCode())) {
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
