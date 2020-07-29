package com.devops.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author yangge
 * @version 1.0.0
 * @title: PackageRecordDTO
 * @date 2020/7/28 9:54
 */
@Data
public class PackageRecordDTO {

    private Integer id;

    private Integer applicationId;

    private String name;

    private String branch;

    private String version;

    /**
     * 1:成功;2:失败
     */
    private String status;

    private Date createDate;

    private String createUser;

    private String applicationName;

    private String filePath;
}
