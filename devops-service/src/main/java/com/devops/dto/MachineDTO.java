package com.devops.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MachineDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String ip;

    private String sshUserName;

    private String sshPassword;

    
}