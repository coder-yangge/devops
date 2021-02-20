package com.devops.web.controller;

import com.devops.common.dto.PageDTO;
import com.devops.common.exception.BizException;
import com.devops.dto.MachineDTO;
import com.devops.service.MachineService;
import com.devops.web.common.vo.ResponseVo;
import com.devops.web.form.MachineForm;
import com.devops.web.vo.MachineVo;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yangge
 * @version 1.0.0
 * @title: MachineController
 * @date 2020/7/15 9:36
 */
@RestController
@RequestMapping("/machine")
@Api(tags = "服务器管理")
public class MachineController {

    @Autowired
    private MachineService machineService;

    @GetMapping("/list")
    public ResponseVo<List<MachineVo>> getMachineList() {
        List<MachineDTO> machineList = machineService.getMachineList();
        List<MachineVo> voList = new ArrayList<>();
        machineList.forEach(machineDTO -> {
            MachineVo vo = new MachineVo();
            vo.setId(machineDTO.getId());
            vo.setName(machineDTO.getName());
            vo.setIp(machineDTO.getIp());
            voList.add(vo);
        });
        return ResponseVo.ResponseBuilder.buildSuccess(voList);
    }

    @PostMapping("/page")
    public ResponseVo<PageDTO<MachineDTO>> getMachineByPage(@RequestBody MachineForm machineForm) {
        PageDTO pageDTO = new PageDTO();
        MachineDTO machineDTO = new MachineDTO();
        BeanUtils.copyProperties(machineForm, machineDTO);
        BeanUtils.copyProperties(machineForm, pageDTO);
        PageDTO<MachineDTO> page = machineService.getByPage(machineDTO, pageDTO);
        return ResponseVo.ResponseBuilder.buildSuccess(page);
    }

    @PutMapping("/save")
    public ResponseVo save(@RequestBody MachineForm machineForm) {
        MachineDTO machineDTO = new MachineDTO();
        BeanUtils.copyProperties(machineForm, machineDTO);
        machineDTO.setId(null);
        machineService.save(machineDTO);
        return ResponseVo.ResponseBuilder.buildSuccess();
    }

    @PutMapping("/edit")
    public ResponseVo edit(@RequestBody MachineForm machineForm) {
        MachineDTO machineDTO = new MachineDTO();
        BeanUtils.copyProperties(machineForm, machineDTO);
        machineService.save(machineDTO);
        return ResponseVo.ResponseBuilder.buildSuccess();
    }

    @DeleteMapping("/{id}")
    public ResponseVo delete(@PathVariable("id") Integer machineId) {
        if (machineId == null) {
            throw new BizException("非法参数");
        }
        machineService.delete(machineId);
        return ResponseVo.ResponseBuilder.buildSuccess();
    }

    @GetMapping("/{id}")
    public ResponseVo<MachineDTO> query(@PathVariable("id") Integer machineId) {
        MachineDTO machineDTO = machineService.getById(machineId);
        return ResponseVo.ResponseBuilder.buildSuccess(machineDTO);
    }

}
