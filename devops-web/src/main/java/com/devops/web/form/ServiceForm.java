package com.devops.web.form;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author yangge
 * @version 1.0.0
 * @title: ServiceForm
 * @description: TODO
 * @date 2020/7/6 12:41
 */
@Data
public class ServiceForm {

    private Integer id;

    private String name;

    private String desc;

    private Integer businessLineId;
}
