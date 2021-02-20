package com.devops.web.form;

import lombok.Data;

/**
 * @author yangge
 * @version 1.0.0
 * @title: DeployForm
 * @date 2020/8/5 10:40
 */
@Data
public class DeployForm {

    private Integer applicationId;

    private String env;

    private Integer packageRecordId;

    private String remark;
}
