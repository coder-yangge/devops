package com.devops.web.form;

import lombok.Data;

/**
 * @author yangge
 * @version 1.0.0
 * @title: BuildForm
 * @date 2020/7/18 14:07
 */
@Data
public class BuildForm {

    private Integer applicationId;

    private String branch;

    private String remark;
}
