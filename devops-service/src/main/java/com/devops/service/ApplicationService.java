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
	
	/**
	 * 保存
	 * @param applicationDto
	 */
	void saveApplicationDto(ApplicationDto applicationDto);
	
	/**
	 * 根据主键id查询
	 * @param id
	 * @return
	 */
	ApplicationDto findById(Integer id);
	
	/**
	 * 根据主键id删除
	 * @param id
	 */
	void deleteById(Integer id);
}
