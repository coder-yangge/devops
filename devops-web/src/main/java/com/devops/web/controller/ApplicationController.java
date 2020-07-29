package com.devops.web.controller;

import com.devops.common.enums.DeployTypeEnum;
import com.devops.dto.*;
import com.devops.event.PullCodeEvent;
import com.devops.service.BusinessLineService;
import com.devops.service.ClusterService;
import com.devops.service.Service;
import com.devops.service.MachineService;
import com.devops.web.vo.ApplicationVo;
import com.devops.web.vo.ServiceApplicationVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.devops.common.dto.PageDTO;
import com.devops.service.ApplicationService;
import com.devops.web.common.vo.ResponseVo;
import com.devops.web.common.vo.ResponseVo.ResponseBuilder;
import com.devops.web.form.ApplicationFrom;
import com.devops.web.form.ApplicationQueryForm;

import io.swagger.annotations.Api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/application")
@Api(tags = "应用管理")
public class ApplicationController {

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private BusinessLineService businessLineService;

	@Autowired
	private Service service;

	@Autowired
	private ClusterService clusterService;

	@Autowired
	private MachineService machineService;

	@Autowired
	private ApplicationEventMulticaster eventMulticaster;

	@ResponseBody
	@PostMapping("/page")
	public ResponseVo<PageDTO<ApplicationDto>> getPage(@RequestBody ApplicationQueryForm queryFrom) {
		ApplicationDto applicationDto = new ApplicationDto();
		PageDTO pageDTO = new PageDTO();
		BeanUtils.copyProperties(queryFrom, applicationDto);
		BeanUtils.copyProperties(queryFrom, pageDTO);
		PageDTO<ApplicationDto> page = applicationService.getPage(applicationDto, pageDTO);
		return ResponseBuilder.buildSuccess(page);
	}

	@ResponseBody
	@PutMapping("/save")
	public ResponseVo<ApplicationDto> saveApplication(@RequestBody ApplicationFrom applicationFrom) {
		ApplicationDto applicationDto = convert(applicationFrom);
		applicationService.saveApplicationDto(applicationDto);
		eventMulticaster.multicastEvent(new PullCodeEvent(applicationDto));
		return ResponseBuilder.buildSuccess();
	}

	@ResponseBody
	@PatchMapping("/edit")
	public ResponseVo<ApplicationDto> editApplication(@RequestBody ApplicationFrom applicationFrom) {
		ApplicationDto applicationDto = convert(applicationFrom);
		applicationService.modifyApplication(applicationDto);
		return ResponseBuilder.buildSuccess();
	}

	@ResponseBody
	@GetMapping("/query/{id}")
	public ResponseVo<ApplicationDto> findById(@PathVariable Integer id) {
		ApplicationDto applicationDto = applicationService.findById(id);
		return ResponseBuilder.buildSuccess(applicationDto);
	}

	@ResponseBody
	@DeleteMapping("/delete/{id}")
	public ResponseVo<ApplicationDto> deleteById(@PathVariable Integer id){
		applicationService.deleteById(id);
		return ResponseBuilder.buildSuccess();
	}

	@GetMapping("/build/index")
	public String build(ModelMap modelMap) {
		List<ApplicationDto> applicationDtoList = applicationService.getApplicationList();
		Map<Integer, List<ApplicationDto>> listMap = applicationDtoList.stream().collect(Collectors.groupingBy(ApplicationDto::getServiceId));
		List<ServiceApplicationVo> applicationVoList = new ArrayList<>();
		listMap.forEach((serviceId, dtoList)->{
			ApplicationDto dto = dtoList.get(0);
			List<ApplicationVo> applicationList = new ArrayList<>();
			dtoList.forEach(e ->{
				ApplicationVo vo = new ApplicationVo();
				vo.setId(e.getId());
				vo.setName(e.getName());
				applicationList.add(vo);
			});
			ServiceApplicationVo vo = new ServiceApplicationVo();
			vo.setId(serviceId);
			vo.setName(dto.getServiceName());
			vo.setApplicationList(applicationList);
			applicationVoList.add(vo);
		});
		modelMap.put("data", applicationVoList);
		return "devops/build/index";
	}


	@GetMapping("/edit/page/{applicationId}")
	public String build(@PathVariable("applicationId") Integer applicationId, ModelMap modelMap) {
		ApplicationDto applicationDto = applicationService.findById(applicationId);
		List<BusinessLineDTO> businessLineDTOList = businessLineService.getAll();
		ServiceDTO query = new ServiceDTO();
		query.setBusinessLineId(applicationDto.getBusinessLineId());
		List<ServiceDTO> serviceDTOS = service.getAll(query);
		if (applicationDto.getEnvironment().getDeployType().equals(DeployTypeEnum.MACHINE.getCode())) {
			List<MachineDTO> machineList = machineService.getMachineList();
			modelMap.put("machineList", machineList);
		} else {
			List<ClusterDTO> clusterList = clusterService.getClusterList();
			modelMap.put("clusterList", clusterList);
		}
		modelMap.put("data", applicationDto);
		modelMap.put("businessLineList", businessLineDTOList);
		modelMap.put("serviceList", serviceDTOS);
		return "devops/application/applicationEdit";
	}

	private ApplicationDto convert(ApplicationFrom applicationFrom) {
		ApplicationDto applicationDto = new ApplicationDto();
		RepositoryDto repositoryDto = new RepositoryDto();
		EnvironmentDto environmentDto = new EnvironmentDto();
		BeanUtils.copyProperties(applicationFrom.getRepository(), repositoryDto);
		BeanUtils.copyProperties(applicationFrom, applicationDto);
		BeanUtils.copyProperties(applicationFrom.getEnvironment(), environmentDto);
		applicationDto.setRepository(repositoryDto);
		applicationDto.setEnvironment(environmentDto);
		repositoryDto.setApplicationId(applicationDto.getId());
		environmentDto.setApplicationId(applicationDto.getId());
		return applicationDto;
	}
}
