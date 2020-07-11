package com.devops.service.impl;

import com.devops.common.dto.PageDTO;
import com.devops.dto.ServiceDTO;
import com.devops.repository.ServiceRepository;
import com.devops.service.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author yangge
 * @version 1.0.0
 * @title: ServiceImpl
 * @description: TODO
 * @date 2020/7/6 12:15
 */
@org.springframework.stereotype.Service
public class ServiceImpl implements Service {

    @Autowired
    private ServiceRepository serviceRepository;


    @Override
    public ServiceDTO save(ServiceDTO dto) {
        com.devops.entity.Service entity = new com.devops.entity.Service();
        BeanUtils.copyProperties(dto, entity);
        entity.setCreateDate(Timestamp.from(Instant.now()));
        entity = serviceRepository.save(entity);
        ServiceDTO serviceDTO = new ServiceDTO();
        BeanUtils.copyProperties(entity, serviceDTO);
        return serviceDTO;
    }

    @Override
    public void delete(Integer id) {
        if (serviceRepository.existsById(id)) {
            serviceRepository.deleteById(id);
        }
    }

    @Override
    public PageDTO<ServiceDTO> queryByPage(ServiceDTO serviceDTO, PageDTO pageDTO) {
        com.devops.entity.Service entity = new com.devops.entity.Service();
        BeanUtils.copyProperties(serviceDTO, entity);
        Pageable pageable = PageRequest.of(pageDTO.getPageNum() -1, pageDTO.getPageSize());
        Page<com.devops.entity.Service> servicePage = serviceRepository.findAll(buildSpecification(serviceDTO), pageable);
        return PageDTO.of(servicePage, ServiceDTO::new, BeanUtils::copyProperties);
    }

    @Override
    public List<ServiceDTO> getAll(ServiceDTO serviceDTO) {
        com.devops.entity.Service entity = new com.devops.entity.Service();
        BeanUtils.copyProperties(serviceDTO, entity);
        List<com.devops.entity.Service> serviceList = serviceRepository.findAll(buildSpecification(serviceDTO));
        List<ServiceDTO> dtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(serviceList)) {
            serviceList.forEach(e ->{
                ServiceDTO dto = new ServiceDTO();
                BeanUtils.copyProperties(e, dto);
                dtoList.add(dto);
            });
        }
        return dtoList;
    }

    @Override
    public ServiceDTO queryById(Integer id) {
        Optional<com.devops.entity.Service> optional = serviceRepository.findById(id);
        if (optional.isPresent()) {
            com.devops.entity.Service service = optional.get();
            ServiceDTO dto = new ServiceDTO();
            BeanUtils.copyProperties(service, dto);
            return dto;
        }
        return null;
    }

    private Specification<com.devops.entity.Service> buildSpecification(ServiceDTO serviceDTO) {
        return (Specification<com.devops.entity.Service>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicate = new ArrayList<>();
            if (StringUtils.isNotBlank(serviceDTO.getName())) {
                predicate.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + serviceDTO.getName() + "%"));
            }
            Predicate[] pre = new Predicate[predicate.size()];
            return criteriaQuery.where(predicate.toArray(pre)).getRestriction();
        };
    }
}
