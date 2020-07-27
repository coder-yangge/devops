package com.devops.web.common.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

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
    @Range(min = 1, message = "pageNum参数不正确")
    private Integer pageNum;

    @ApiModelProperty(name = "pageSize", notes = "每页数据数", example = "10")
    @NotNull
    @Range(min = 1, message = "pageSize参数不正确")
    private Integer pageSize;
}
