package com.devops.web.controller;

import com.devops.common.dto.PageDTO;
import com.devops.dto.BusinessLineDTO;
import com.devops.service.BusinessLineService;
import com.devops.web.common.vo.ResponseVo;
import com.devops.web.common.vo.ResponseVo.ResponseBuilder;
import com.devops.web.form.BusinessLineForm;
import com.devops.web.form.BusinessLineQueryForm;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yangge
 * @version 1.0.0
 * @title: BusinessLineController
 * @description: TODO
 * @date 2020/7/5 14:16
 */
@RestController
@RequestMapping("/business/line")
@Api(tags = "业务线管理")
public class BusinessLineController {

    @Autowired
    private BusinessLineService businessLineService;

    @PostMapping("/page")
    public ResponseVo<PageDTO<BusinessLineDTO>> getPage(@RequestBody BusinessLineQueryForm queryFrom) {
        BusinessLineDTO businessLineDTO = new BusinessLineDTO();
        PageDTO pageDTO = new PageDTO();
        BeanUtils.copyProperties(queryFrom, businessLineDTO);
        BeanUtils.copyProperties(queryFrom, pageDTO);
        PageDTO<BusinessLineDTO> page = businessLineService.getPage(businessLineDTO, pageDTO);
        return ResponseBuilder.buildSuccess(page);
    }

    @PutMapping("/save")
    public ResponseVo saveBusinessLine(@RequestBody BusinessLineForm businessLineForm) {
        BusinessLineDTO businessLineDTO = new BusinessLineDTO();
        BeanUtils.copyProperties(businessLineForm, businessLineDTO);
        businessLineService.saveBusinessLine(businessLineDTO);
        return ResponseBuilder.buildSuccess();
    }

    @PatchMapping("/edit")
    public ResponseVo editBusinessLine(@RequestBody BusinessLineForm businessLineForm) {
        BusinessLineDTO businessLineDTO = new BusinessLineDTO();
        BeanUtils.copyProperties(businessLineForm, businessLineDTO);
        businessLineService.saveBusinessLine(businessLineDTO);
        return ResponseBuilder.buildSuccess();
    }

    @GetMapping("/query/{id}")
    public ResponseVo editBusinessLine(@PathVariable Integer id) {
        BusinessLineDTO businessLineDTO = businessLineService.queryById(id);
        return ResponseBuilder.buildSuccess(businessLineDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseVo deleteBusinessLine(@PathVariable Integer id) {
        businessLineService.deleteById(id);
        return ResponseBuilder.buildSuccess();
    }

    @GetMapping("/query")
    public ResponseVo getBusinessLineList() {
        List<BusinessLineDTO> businessLineDTOS = businessLineService.getAll();
        return ResponseBuilder.buildSuccess(businessLineDTOS);
    }
}
