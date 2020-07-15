package com.devops.web.form;

import lombok.Data;

/**
 * @author yangge
 * @version 1.0.0
 * @title: RepositoryForm
 * @date 2020/7/14 18:59
 */
@Data
public class RepositoryForm {

    private Integer id;

    private Integer type;

    private String address;

    private String userName;

    private String passWord;

}
