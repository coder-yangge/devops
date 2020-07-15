package com.devops.web.form;

import lombok.Data;

/**
 * @author yangge
 * @version 1.0.0
 * @title: EnvironmentForm
 * @date 2020/7/14 19:02
 */
@Data
public class EnvironmentForm {

    private String name;

    private Integer deployType;

    private Integer deployId;

    private String deployPath;

    private String startScript;
}
