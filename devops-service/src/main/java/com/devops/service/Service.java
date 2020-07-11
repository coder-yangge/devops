package com.devops.service;

import com.devops.common.dto.PageDTO;
import com.devops.dto.ServiceDTO;

import java.util.List;

/**
 * @author yangge
 * @version 1.0.0
 * @title: Service
 * @description: TODO
 * @date 2020/7/6 12:15
 */
public interface Service {

    ServiceDTO save(ServiceDTO dto);

    void delete(Integer id);

    PageDTO<ServiceDTO> queryByPage(ServiceDTO serviceDTO, PageDTO pageDTO);

    List<ServiceDTO> getAll(ServiceDTO serviceDTO);

    ServiceDTO queryById(Integer id);
}
