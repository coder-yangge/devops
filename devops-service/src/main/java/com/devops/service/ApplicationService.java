package com.devops.service;

import com.devops.common.dto.PageDTO;
import com.devops.dto.ApplicationDto;

public interface ApplicationService {
	
	/**
	 * 分页查询
	 * @param applicationDto
	 * @param pageDTO
	 * @return
	 */
	PageDTO<ApplicationDto> getPage(ApplicationDto applicationDto, PageDTO pageDTO);
}
