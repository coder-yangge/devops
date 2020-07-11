package com.devops.service.impl;

import com.devops.common.dto.PageDTO;
import com.devops.common.exception.BizException;
import com.devops.dto.BusinessLineDTO;
import com.devops.entity.BusinessLine;
import com.devops.repository.BusinessLineRepository;
import com.devops.service.BusinessLineService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author yangge
 * @version 1.0.0
 * @title: BusinessLineServiceImpl
 * @description: TODO
 * @date 2020/7/5 12:39
 */
@Service
public class BusinessLineServiceImpl implements BusinessLineService {

    @Autowired
    private BusinessLineRepository businessLineRepository;

    @Override
    public PageDTO<BusinessLineDTO> getPage(BusinessLineDTO businessLineDTO, PageDTO pageDTO) {
        BusinessLine query = new BusinessLine();
        BeanUtils.copyProperties(businessLineDTO, query);
        Pageable pageable = PageRequest.of(pageDTO.getPageNum() -1, pageDTO.getPageSize());
        Specification<BusinessLine> specification = (Specification<BusinessLine>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicate = new ArrayList<>();
            if (StringUtils.isNotBlank(businessLineDTO.getName())) {
                predicate.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + businessLineDTO.getName() + "%"));
            }
            Predicate[] pre = new Predicate[predicate.size()];
            return criteriaQuery.where(predicate.toArray(pre)).getRestriction();
        };
        Page<BusinessLine> businessLinePage = businessLineRepository.findAll(specification, pageable);
        return PageDTO.of(businessLinePage, BusinessLineDTO::new, BeanUtils::copyProperties);
    }

    @Override
    public void saveBusinessLine(BusinessLineDTO businessLineDTO) {
        BusinessLine entity = new BusinessLine();
        BeanUtils.copyProperties(businessLineDTO, entity);
        entity.setCreateDate(Timestamp.from(Instant.now()));
        businessLineRepository.save(entity);
    }

    @Override
    public BusinessLineDTO queryById(Integer id) {
        Optional<BusinessLine> optional = businessLineRepository.findById(id);
        if (optional.isPresent()) {
            BusinessLineDTO businessLineDTO = new BusinessLineDTO();
            BeanUtils.copyProperties(optional.get(), businessLineDTO);
            return businessLineDTO;
        }
        return null;
    }

    @Override
    public void deleteById(Integer id) {
        if (businessLineRepository.existsById(id)) {
            new BizException("业务线不存在");
        }
        businessLineRepository.deleteById(id);
    }
}
