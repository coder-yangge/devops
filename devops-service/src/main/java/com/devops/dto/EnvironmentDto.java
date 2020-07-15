package com.devops.dto;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author yangge
 * @version 1.0.0
 * @title: EnvironmentDto
 * @date 2020/7/14 18:12
 */
@Data
public class EnvironmentDto {

    private Integer id;

    private Integer applicationId;

    private String name;

    /**
     * 1:服务器;2:集群
     */
    private Integer deployType;

    private Integer deployId;

    private String deployPath;

    private String startScript;

    private String packageCommand;

    private String packageArg;

    private String packagePath;

    private Timestamp createDate;

    private String createUser;

}
