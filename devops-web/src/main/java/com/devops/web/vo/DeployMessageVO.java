package com.devops.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author yangge
 * @version 1.0.0
 * @title: DeployMessageVO
 * @date 2020/8/2 22:37
 */
@Data
@Builder
@ApiModel("发布应用消息")
public class DeployMessageVO {

    @ApiModelProperty(name = "应用ID")
    private Integer applicationId;

    @ApiModelProperty(name = "ip")
    private String ip;

    @ApiModelProperty(name = "消息颜色")
    private String color;

    @ApiModelProperty(name = "消息")
    private String message;
}
