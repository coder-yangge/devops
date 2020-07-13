package com.devops.web.form;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ApplicationFrom {
	
	private Integer id;
	
    private String name;

    private String desc;

    private Integer businessLineId;

    private Integer serviceId;

}
