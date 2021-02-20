package com.devops.web.form;

import com.devops.web.common.form.PageForm;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author yangge
 * @version 1.0.0
 * @title: MachineForm
 * @date 2020/8/5 14:05
 */
@Data
@ApiModel("服务器表单")
public class MachineForm extends PageForm {

    private Integer id;

    private String name;

    private String ip;

    private String sshUserName;

    private String sshPassword;

}
