package com.devops.dto;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author yangge
 * @version 1.0.0
 * @title: RepositoryDto
 * @date 2020/7/14 18:10
 */
@Data
public class RepositoryDto {

    private Integer id;

    private Integer applicationId;

    /**
     * 1:git;2:svn;3:本地
     */
    private Integer type;

    private String address;

    private String userName;

    private String password;

    private Timestamp createDate;

    private String createUser;
}
