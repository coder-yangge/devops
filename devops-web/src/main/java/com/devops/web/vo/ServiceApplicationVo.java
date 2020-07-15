package com.devops.web.vo;

import lombok.Data;

import java.util.List;

/**
 * @author yangge
 * @version 1.0.0
 * @title: ServiceApplicationVo
 * @date 2020/7/15 12:48
 */
@Data
public class ServiceApplicationVo {

    private Integer id;

    private String name;

    List<ApplicationVo> applicationList;
}
