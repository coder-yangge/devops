package com.devops.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.devops.common.dto.PageDTO;
import com.devops.dto.ApplicationDto;
import com.devops.entity.Application;
import com.devops.repository.ApplicationRepository;
import com.devops.service.ApplicationService;
import com.devops.service.specification.ApplicationSpecs;

@Service
public class ApplicationServiceImpl implements ApplicationService{
	
	@Autowired
	private ApplicationRepository applicationRepository;
	
	@Override
	public PageDTO<ApplicationDto> getPage(ApplicationDto applicationDto, PageDTO pageDTO) {
		Application application = new Application();
		BeanUtils.copyProperties(applicationDto, application);
        Pageable pageable = PageRequest.of(pageDTO.getPageNum() -1, pageDTO.getPageSize());
        Specification<Application> specification = ApplicationSpecs.getPage(applicationDto);
        Page<Application> applicationPage = applicationRepository.findAll(specification, pageable);
        return PageDTO.of(applicationPage, ApplicationDto::new, BeanUtils::copyProperties);
	}

}
