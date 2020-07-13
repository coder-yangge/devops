package com.devops.dto;

import java.sql.Timestamp;
import java.util.Date;

import lombok.Data;

@Data
public class ApplicationDto {

    private Integer id;

    private String name;

    private String desc;

    private Integer businessLineId;

    private Integer serviceId;

    private Date createDate;

    private String createUser;
}
