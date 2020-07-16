package com.devops.web.form;


import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ApplicationFrom {
	
	private Integer id;

	@NotEmpty(message = "名称不能为空")
    private String name;

    @NotEmpty(message = "描述不能为空")
    private String desc;

    @NotNull(message = "业务线不能为空")
    private Integer businessLineId;

    @NotNull(message = "服务不能为空")
    private Integer serviceId;

    @NotNull(message = "repository字段不能为空")
    private RepositoryForm repository;

    @NotNull(message = "environment字段不能为空")
    private EnvironmentForm environment;

}
