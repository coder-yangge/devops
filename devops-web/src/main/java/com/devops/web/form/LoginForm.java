package com.devops.web.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author yangge
 * @version 1.0.0
 * @title: LoginForm
 * @date 2020/7/31 20:33
 */
@Data
public class LoginForm {

    @NotEmpty(message = "用户名不能为空")
    private String userName;

    @NotEmpty(message = "请输入密码")
    private String password;
}
