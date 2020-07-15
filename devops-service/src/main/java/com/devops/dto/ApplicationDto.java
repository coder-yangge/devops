package com.devops.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ApplicationDto {

    private Integer id;

    private String name;

    private String desc;

    private Integer businessLineId;

    private String businessLineName;

    private String serviceName;

    private Integer serviceId;

    private Date createDate;

    private String createUser;

    private RepositoryDto repository;

    private EnvironmentDto environment;
}
