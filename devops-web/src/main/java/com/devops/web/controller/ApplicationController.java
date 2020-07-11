package com.devops.web.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devops.common.dto.PageDTO;
import com.devops.dto.ApplicationDto;
import com.devops.service.ApplicationService;
import com.devops.web.common.vo.ResponseVo;
import com.devops.web.common.vo.ResponseVo.ResponseBuilder;
import com.devops.web.form.ApplicationQueryForm;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/application")
@Api(tags = "应用管理")
public class ApplicationController {

	@Autowired
	private ApplicationService applicationService;

	@PostMapping("/page")
	public ResponseVo<PageDTO<ApplicationDto>> getPage(@RequestBody ApplicationQueryForm queryFrom) {
		ApplicationDto applicationDto = new ApplicationDto();
		PageDTO pageDTO = new PageDTO();
		BeanUtils.copyProperties(queryFrom, applicationDto);
		BeanUtils.copyProperties(queryFrom, pageDTO);
		PageDTO<ApplicationDto> page = applicationService.getPage(applicationDto, pageDTO);
		return ResponseBuilder.buildSuccess(page);
	}
}
