package com.devops.service.impl;

import com.devops.common.constants.SystemProperties;
import com.devops.common.dto.PageDTO;
import com.devops.common.exception.BizException;
import com.devops.dto.PackageRecordDTO;
import com.devops.entity.Application;
import com.devops.entity.PackageRecord;
import com.devops.repository.ApplicationRepository;
import com.devops.repository.PackageRecordRepository;
import com.devops.service.PackageRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

/**
 * @author yangge
 * @version 1.0.0
 * @title: PackageRecordServiceImpl
 * @date 2020/7/28 10:02
 */
@Service
@Slf4j
public class PackageRecordServiceImpl implements PackageRecordService {

    @Autowired
    private PackageRecordRepository packageRecordRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Value("${application.package.save-location}")
    private String packageSaveLocation;

    @Override
    public PageDTO<PackageRecordDTO> getPackageRecordPage(PageDTO pageDTO) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum() -1, pageDTO.getPageSize());
        Page<PackageRecord> recordPage = packageRecordRepository.findAll(pageable);
        return PageDTO.of(recordPage, PackageRecordDTO::new, BeanUtils::copyProperties);
    }

    @Override
    public void deleteRecord(Integer recordId) {
        Optional<PackageRecord> recordOptional = packageRecordRepository.findById(recordId);
        if (!recordOptional.isPresent()) {
            throw new BizException("记录不存在");
        }
        Integer id = recordOptional.get().getId();
        Integer applicationId = recordOptional.get().getApplicationId();
        String filePath = recordOptional.get().getFilePath();
        packageRecordRepository.deleteById(recordId);
        // 删除对应的部署包
        File file = new File(filePath);
        log.info("开始删除应用ID[{}]构建记录ID[{}]的部署包路径为[{}]", applicationId, id, filePath);
        if (file.exists()) {
            file.delete();
            log.info("应用ID[{}]构建记录ID[{}]的部署包删除成功", applicationId, id, filePath);
            log.info("开始删除应用ID[{}]构建记录ID[{}]的部署包的父目录", applicationId, id, filePath);
            File parentFile = file.getParentFile();
            parentFile.delete();
            log.info("删除应用ID[{}]构建记录ID[{}]的部署包的父目录成功", applicationId, id, filePath);
        } else {
            log.info("删除应用ID[{}]构建记录ID[{}]的部署包路径为[{}]失败，文件不存在", applicationId, id, filePath);
        }
    }
}
