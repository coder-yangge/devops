package com.devops.web.controller;

import com.devops.common.dto.PageDTO;
import com.devops.dto.ServiceDTO;
import com.devops.service.Service;
import com.devops.web.common.vo.ResponseVo;
import com.devops.web.common.vo.ResponseVo.ResponseBuilder;
import com.devops.web.form.ServiceForm;
import com.devops.web.form.ServiceQueryForm;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author yangge
 * @version 1.0.0
 * @title: ServiceController
 * @description: TODO
 * @date 2020/7/6 12:39
 */
@Api(tags = "服务管理")
@RestController
@RequestMapping("/service")
public class ServiceController {
    
    @Autowired
    private Service service;

    @PostMapping("/page")
    public ResponseVo<PageDTO<ServiceDTO>> getPage(@RequestBody ServiceQueryForm queryFrom) {
        ServiceDTO serviceDTO = new ServiceDTO();
        PageDTO pageDTO = new PageDTO();
        BeanUtils.copyProperties(queryFrom, serviceDTO);
        BeanUtils.copyProperties(queryFrom, pageDTO);
        PageDTO<ServiceDTO> page = service.queryByPage(serviceDTO, pageDTO);
        return ResponseBuilder.buildSuccess(page);
    }

    @PutMapping("/save")
    public ResponseVo saveBusinessLine(@RequestBody ServiceForm serviceForm) {
        ServiceDTO serviceDTO = new ServiceDTO();
        BeanUtils.copyProperties(serviceForm, serviceDTO);
        service.save(serviceDTO);
        return ResponseBuilder.buildSuccess();
    }

    @PatchMapping("/edit")
    public ResponseVo editBusinessLine(@RequestBody ServiceForm serviceForm) {
        ServiceDTO serviceDTO = new ServiceDTO();
        BeanUtils.copyProperties(serviceForm, serviceDTO);
        service.save(serviceDTO);
        return ResponseBuilder.buildSuccess();
    }

    @GetMapping("/query/{id}")
    public ResponseVo editBusinessLine(@PathVariable Integer id) {
        ServiceDTO ServiceDTO = service.queryById(id);
        return ResponseBuilder.buildSuccess(ServiceDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseVo deleteBusinessLine(@PathVariable Integer id) {
        service.delete(id);
        return ResponseBuilder.buildSuccess();
    }
}
