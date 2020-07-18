package com.devops.dto;

import lombok.Data;

/**
 * @author yangge
 * @version 1.0.0
 * @title: BuildDTO
 * @date 2020/7/18 16:17
 */
@Data
public class BuildDTO {

    private Integer applicationId;

    private String branch;

    private String remark;
}
