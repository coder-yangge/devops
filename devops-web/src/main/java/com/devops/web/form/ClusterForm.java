package com.devops.web.form;

import lombok.Data;

import java.util.List;

/**
 * @author yangge
 * @version 1.0.0
 * @title: ClusterForm
 * @date 2020/8/5 16:28
 */
@Data
public class ClusterForm {

    private Integer id;

    private String name;

    private String desc;

    private List<MachineForm> machineList;

}
