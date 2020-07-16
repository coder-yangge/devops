package com.devops.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.devops.dto.*;
import com.devops.entity.BusinessLine;
import com.devops.entity.Environment;
import com.devops.entity.Repository;
import com.devops.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.devops.common.dto.PageDTO;
import com.devops.common.exception.BizException;
import com.devops.entity.Application;
import com.devops.service.ApplicationService;
import com.devops.service.specification.ApplicationSpecs;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class ApplicationServiceImpl implements ApplicationService{
	
	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private ServiceRepository serviceRepository;

	@Autowired
	private BusinessLineRepository businessLineRepository;

	@Autowired
	private RepositoryRepository repositoryRepository;

	@Autowired
	private EnvironmentRepository environmentRepository;
	
	@Override
	public PageDTO<ApplicationDto> getPage(ApplicationDto applicationDto, PageDTO pageDTO) {
		Application application = new Application();
		BeanUtils.copyProperties(applicationDto, application);
        Pageable pageable = PageRequest.of(pageDTO.getPageNum() -1, pageDTO.getPageSize());
        Specification<Application> specification = ApplicationSpecs.getPage(applicationDto);
        Page<Application> applicationPage = applicationRepository.findAll(specification, pageable);
		List<Application> content = applicationPage.getContent();
		List<ApplicationDto> dtoList = new ArrayList<>();
		convert(content, dtoList, false);
		return PageDTO.of(applicationPage, dtoList);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveApplicationDto(ApplicationDto applicationDto) {
		if (this.exist(applicationDto.getName())) {
			throw new BizException("应用已存在");
		}
		Application application = new Application();
		BeanUtils.copyProperties(applicationDto, application);
		Optional<com.devops.entity.Service> optionalService = serviceRepository.findById(applicationDto.getServiceId());
		Optional<BusinessLine> optionalBusinessLine = businessLineRepository.findById(applicationDto.getBusinessLineId());
		if (!optionalService.isPresent()) {
			throw new BizException("服务不存在");
		}
		if (!optionalBusinessLine.isPresent()) {
			throw new BizException("业务线不存在");
		}
		application.setBusinessLine(optionalBusinessLine.get());
		application.setService(optionalService.get());
		application.setCreateDate(new Date());
		Application entity = applicationRepository.save(application);
		RepositoryDto repositoryDto = applicationDto.getRepository();
		Repository repository = new Repository();
		BeanUtils.copyProperties(repositoryDto, repository);
		repository.setApplicationId(entity.getId());
		repository.setCreateDate(Timestamp.from(Instant.now()));
		repositoryRepository.save(repository);
		Environment environment = new Environment();
		BeanUtils.copyProperties(applicationDto.getEnvironment(), environment);
		environment.setApplicationId(entity.getId());
		environment.setCreateDate(Timestamp.from(Instant.now()));
		environmentRepository.save(environment);
	}

	@Override
	public ApplicationDto findById(Integer id) {
		Optional<Application> applicationOptional = applicationRepository.findById(id);
		if(applicationOptional.isPresent()) {
			ApplicationDto applicationDto = new ApplicationDto();
			Application application = applicationOptional.get();
			convert(application, applicationDto, true);
			return applicationDto;
		}
		return null;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void deleteById(Integer id) {
		if(applicationRepository.existsById(id)) {
			new BizException("应用管理不存在");
		}
		applicationRepository.deleteById(id);
		repositoryRepository.deleteByApplicationId(id);
		environmentRepository.deleteByApplicationId(id);
	}

	@Override
	public List<ApplicationDto> getApplicationList() {
		List<Application> applicationList = applicationRepository.findAll();
		List<ApplicationDto> dtoList = new ArrayList<>();
		convert(applicationList, dtoList, true);
		return dtoList;
	}


	@Transactional(rollbackFor = Exception.class)
	@Override
	public void modifyApplication(ApplicationDto applicationDto) {
		Application application = new Application();
		if (this.exist(applicationDto.getName())) {
			throw new BizException("应用已存在");
		}
		Optional<com.devops.entity.Service> optionalService = serviceRepository.findById(applicationDto.getServiceId());
		Optional<BusinessLine> optionalBusinessLine = businessLineRepository.findById(applicationDto.getBusinessLineId());
		Optional<Environment> optionalEnvironment = environmentRepository.findById(applicationDto.getEnvironment().getId());
		Optional<Repository> repositoryOptional = repositoryRepository.findById(applicationDto.getRepository().getId());
		if (!optionalService.isPresent()) {
			throw new BizException("服务不存在");
		}
		if (!optionalBusinessLine.isPresent()) {
			throw new BizException("业务线不存在");
		}
		if (!optionalEnvironment.isPresent()) {
			throw new BizException("环境不存在");
		}
		if (!repositoryOptional.isPresent()) {
			throw new BizException("仓库不存在");
		}
		Repository repository = new Repository();
		Environment environment = new Environment();
		RepositoryDto repositoryDto = applicationDto.getRepository();
		BeanUtils.copyProperties(applicationDto, application);
		BeanUtils.copyProperties(repositoryDto, repository);
		BeanUtils.copyProperties(applicationDto.getEnvironment(), environment);
		application.setBusinessLine(optionalBusinessLine.get());
		application.setService(optionalService.get());
		application.setCreateDate(new Date());
		repository.setCreateDate(Timestamp.from(Instant.now()));
		environment.setCreateDate(Timestamp.from(Instant.now()));
		applicationRepository.save(application);
		repositoryRepository.save(repository);
		environmentRepository.save(environment);
	}

	@Override
	public boolean exist(String name) {
		Application application = new Application();
		application.setName(name);
		List<Application> list = applicationRepository.findAll(Example.of(application));
		return !CollectionUtils.isEmpty(list);
	}

	private void convert(Application application, ApplicationDto applicationDto, boolean deep) {
		BeanUtils.copyProperties(application, applicationDto);
		applicationDto.setBusinessLineId(application.getBusinessLine().getId());
		applicationDto.setBusinessLineName(application.getBusinessLine().getName());
		applicationDto.setServiceId(application.getService().getId());
		applicationDto.setServiceName(application.getService().getName());
		if (deep) {
			RepositoryDto repositoryDto = new RepositoryDto();
			EnvironmentDto environmentDto = new EnvironmentDto();
			Repository repository = repositoryRepository.findByApplicationId(application.getId());
			Environment environment = environmentRepository.findByApplicationId(application.getId());
			BeanUtils.copyProperties(repository, repositoryDto);
			BeanUtils.copyProperties(environment, environmentDto);
			applicationDto.setEnvironment(environmentDto);
			applicationDto.setRepository(repositoryDto);
		}
	}

	private void convert(List<Application> applicationList, List<ApplicationDto> dtoList, boolean deep) {
		if (!CollectionUtils.isEmpty(applicationList)) {
			applicationList.forEach(e ->{
				ApplicationDto dto = new ApplicationDto();
				convert(e, dto, deep);
				dtoList.add(dto);
			});
		}

	}

}
