package com.devops.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class BusinessLineDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String desc;

    private Timestamp createDate;

    private String createUser;

    
}