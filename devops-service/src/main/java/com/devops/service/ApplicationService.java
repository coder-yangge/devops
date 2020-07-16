package com.devops.service;

import com.devops.common.dto.PageDTO;
import com.devops.dto.ApplicationDto;

import java.util.List;

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

	/**
	 * 查询列表
	 * @return
	 */
	List<ApplicationDto> getApplicationList();

	void modifyApplication(ApplicationDto applicationDto);

	boolean exist(String name);
}
