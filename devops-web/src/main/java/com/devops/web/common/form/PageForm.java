package com.devops.web.common.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author yangge
 * @version 1.0.0
 * @title: PageFrom
 * @date 2020/7/5 12:32
 */
@Data
@ApiModel("分页表单")
public class PageForm {

    @ApiModelProperty(name = "pageNum", notes = "查询页", example = "1")
    @NotNull
    private Integer pageNum;

    @ApiModelProperty(name = "pageSize", notes = "每页数据数", example = "10")
    @NotNull
    private Integer pageSize;
}
