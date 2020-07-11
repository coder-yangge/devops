package com.devops.service;

import com.devops.common.dto.PageDTO;
import com.devops.dto.BusinessLineDTO;

/**
 * @author yangge
 * @version 1.0.0
 * @title: BusinessLineService
 * @date 2020/7/5 12:36
 */
public interface BusinessLineService {

    PageDTO<BusinessLineDTO> getPage(BusinessLineDTO businessLineDTO, PageDTO pageDTO);

    void saveBusinessLine(BusinessLineDTO businessLineDTO);

    BusinessLineDTO queryById(Integer id);

    void deleteById(Integer id);
}
