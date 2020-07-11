package com.devops.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClusterDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String desc;

    
}