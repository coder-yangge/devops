package com.devops.web.form;

import com.devops.web.common.form.PageForm;

import lombok.Data;

@Data
public class ApplicationQueryForm extends PageForm{
	
	private String name;

	private Integer businessLineId;

	private Integer serviceId;
}
