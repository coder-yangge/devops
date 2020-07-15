package com.devops.web.controller;

import com.devops.dto.EnvironmentDto;
import com.devops.dto.RepositoryDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devops.common.dto.PageDTO;
import com.devops.dto.ApplicationDto;
import com.devops.service.ApplicationService;
import com.devops.web.common.vo.ResponseVo;
import com.devops.web.common.vo.ResponseVo.ResponseBuilder;
import com.devops.web.form.ApplicationFrom;
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

	@PutMapping("/save")
	public ResponseVo<ApplicationDto> saveApplicationDto(@RequestBody ApplicationFrom applicationFrom) {
		ApplicationDto applicationDto = new ApplicationDto();
		RepositoryDto repositoryDto = new RepositoryDto();
		EnvironmentDto environmentDto = new EnvironmentDto();
		BeanUtils.copyProperties(applicationFrom.getRepository(), repositoryDto);
		BeanUtils.copyProperties(applicationFrom, applicationDto);
		BeanUtils.copyProperties(applicationFrom.getEnvironment(), environmentDto);
		applicationDto.setRepository(repositoryDto);
		applicationDto.setEnvironment(environmentDto);
		applicationService.saveApplicationDto(applicationDto);
		return ResponseBuilder.buildSuccess();
	}

	@PatchMapping("/edit")
	public ResponseVo<ApplicationDto> editBusinessLine(@RequestBody ApplicationFrom applicationFrom) {
		ApplicationDto applicationDto = new ApplicationDto();
		BeanUtils.copyProperties(applicationFrom, applicationDto);
		applicationService.saveApplicationDto(applicationDto);
		return ResponseBuilder.buildSuccess();
	}
	
	@GetMapping("/query/{id}")
	public ResponseVo<ApplicationDto> findById(@PathVariable Integer id) {
		ApplicationDto applicationDto = applicationService.findById(id);
		return ResponseBuilder.buildSuccess(applicationDto);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseVo<ApplicationDto> deleteById(@PathVariable Integer id){
		applicationService.deleteById(id);
		return ResponseBuilder.buildSuccess();
	}
}
