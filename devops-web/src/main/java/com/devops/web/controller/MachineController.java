package com.devops.web.controller;

import com.devops.dto.MachineDTO;
import com.devops.service.impl.MachineService;
import com.devops.web.common.vo.ResponseVo;
import com.devops.web.vo.MachineVo;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
