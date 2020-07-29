package com.devops.service;

import com.devops.common.dto.PageDTO;
import com.devops.dto.PackageRecordDTO;

/**
 * @author yangge
 * @version 1.0.0
 * @title: PackageRecordService
 * @date 2020/7/28 9:56
 */
public interface PackageRecordService {

    PageDTO<PackageRecordDTO> getPackageRecordPage(PageDTO pageDTO);

    void deleteRecord(Integer recordId);
}
