package com.devops.web.controller;

import com.devops.common.constants.SystemProperties;
import com.devops.common.dto.PageDTO;
import com.devops.dto.PackageRecordDTO;
import com.devops.service.PackageRecordService;
import com.devops.web.common.form.PageForm;
import com.devops.web.common.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @author yangge
 * @version 1.0.0
 * @title: PackageRecordController
 * @date 2020/7/28 10:04
 */
@RestController
@RequestMapping("/package/record")
public class PackageRecordController {

    @Autowired
    private PackageRecordService packageRecordService;

    @PostMapping("/page")
    public ResponseVo getRecordPage(@RequestBody PageForm pageForm) {
        PageDTO pageDTO = new PageDTO();
        BeanUtils.copyProperties(pageForm, pageDTO);
        PageDTO<PackageRecordDTO> packageRecordPage = packageRecordService.getPackageRecordPage(pageDTO);
        return ResponseVo.ResponseBuilder.buildSuccess(packageRecordPage);
    }

    @DeleteMapping("/{id}")
    public ResponseVo getRecordPage(@PathVariable("id") Integer recordId) {
        packageRecordService.deleteRecord(recordId);
        return ResponseVo.ResponseBuilder.buildSuccess();
    }
}
