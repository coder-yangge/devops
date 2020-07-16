package com.devops.service;

import com.devops.dto.EnvironmentDto;

/**
 * @author yangge
 * @version 1.0.0
 * @title: EnvironmentService
 * @date 2020/7/16 18:35
 */
public interface EnvironmentService {

    EnvironmentDto getByApplication(Integer applicationId);
}
