package com.devops.service.impl;

import com.devops.dto.EnvironmentDto;
import com.devops.entity.Environment;
import com.devops.repository.EnvironmentRepository;
import com.devops.service.EnvironmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * @author yangge
 * @version 1.0.0
 * @title: EnvironmentServiceImpl
 * @date 2020/7/16 18:35
 */
@Service
public class EnvironmentServiceImpl implements EnvironmentService {

    @Autowired
    private EnvironmentRepository environmentRepository;

    @Override
    public EnvironmentDto getByApplication(Integer applicationId) {
        Environment environment = environmentRepository.findByApplicationId(applicationId);
        if (ObjectUtils.isEmpty(environment)) {
            return null;
        }
        EnvironmentDto environmentDto = new EnvironmentDto();
        BeanUtils.copyProperties(environment, environmentDto);
        return environmentDto;
    }
}
