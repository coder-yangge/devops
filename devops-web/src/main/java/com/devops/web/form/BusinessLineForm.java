package com.devops.web.form;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author yangge
 * @version 1.0.0
 * @title: BusinessForm
 * @description: TODO
 * @date 2020/7/5 18:20
 */
@Data
public class BusinessLineForm {

    private Integer id;

    private String name;

    private String desc;

    private Timestamp createDate;

    private String createUser;
}
