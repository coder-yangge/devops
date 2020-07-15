package com.devops.web.vo;

import lombok.Data;

/**
 * @author yangge
 * @version 1.0.0
 * @title: MachineVo
 * @date 2020/7/15 9:38
 */
@Data
public class MachineVo {

    private Integer id;

    private String name;

    private String ip;

    private String sshUserName;

    private String sshPassword;

}
