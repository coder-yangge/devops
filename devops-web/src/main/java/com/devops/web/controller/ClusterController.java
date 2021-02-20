package com.devops.web.controller;

import com.devops.common.dto.PageDTO;
import com.devops.dto.ClusterDTO;
import com.devops.dto.MachineDTO;
import com.devops.service.ClusterService;
import com.devops.web.common.vo.ResponseVo;
import com.devops.web.form.ClusterForm;
import com.devops.web.form.ClusterQueryForm;
import com.devops.web.form.MachineForm;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yangge
 * @version 1.0.0
 * @title: ClusterController
 * @date 2020/7/15 10:01
 */
@RestController
@RequestMapping("/cluster")
@Api(tags = "集群管理")
@Slf4j
public class ClusterController {

    @Autowired
    private ClusterService clusterService;

    @GetMapping("/list")
    public ResponseVo<List<ClusterDTO>> getClusterList() {
        List<ClusterDTO> clusterList = clusterService.getClusterList();
        return ResponseVo.ResponseBuilder.buildSuccess(clusterList);
    }

    @PostMapping("/page")
    public ResponseVo<PageDTO<ClusterDTO>> getClusterPage(@RequestBody ClusterQueryForm clusterForm) {
        ClusterDTO clusterDTO = new ClusterDTO();
        PageDTO pageDTO = new PageDTO();
        BeanUtils.copyProperties(clusterForm, clusterDTO);
        BeanUtils.copyProperties(clusterForm, pageDTO);
        PageDTO<ClusterDTO> clusterPageDto = clusterService.getPage(clusterDTO, pageDTO);
        return ResponseVo.ResponseBuilder.buildSuccess(clusterPageDto);
    }

    @PutMapping("")
    public ResponseVo<PageDTO<ClusterDTO>> save(@RequestBody ClusterForm clusterForm) {
        ClusterDTO clusterDTO = new ClusterDTO();
        BeanUtils.copyProperties(clusterForm, clusterDTO);
        List<MachineForm> machineList = clusterForm.getMachineList();
        if (!CollectionUtils.isEmpty(machineList)) {
            List<MachineDTO> machineDTOList = new ArrayList<>();
            machineList.forEach(machineForm -> {
                if (machineForm.getId() != null) {
                    MachineDTO machineDTO = new MachineDTO();
                    machineDTO.setId(machineForm.getId());
                    machineDTOList.add(machineDTO);
                }
               clusterDTO.setMachineList(machineDTOList);
            });
        }
        clusterService.save(clusterDTO);
        return ResponseVo.ResponseBuilder.buildSuccess();
    }

    @DeleteMapping("/{id}")
    public ResponseVo delete(@PathVariable("id") Integer id){
        clusterService.delete(id);
        return ResponseVo.ResponseBuilder.buildSuccess();
    }

    @GetMapping("/{id}")
    public ResponseVo<ClusterDTO> getClusterPage(@PathVariable("id") Integer id) {
        ClusterDTO clusterDTO = clusterService.getById(id);
        if (clusterDTO == null) {
            return ResponseVo.ResponseBuilder.buildFailed("数据不存在");
        }
        return ResponseVo.ResponseBuilder.buildSuccess(clusterDTO);
    }

    @PatchMapping("")
    public ResponseVo<PageDTO<ClusterDTO>> update(@RequestBody ClusterForm clusterForm) {
        ClusterDTO clusterDTO = new ClusterDTO();
        BeanUtils.copyProperties(clusterForm, clusterDTO);
        if (!CollectionUtils.isEmpty(clusterForm.getMachineList())) {
            List<MachineDTO> machineList = new ArrayList<>();
            clusterForm.getMachineList().forEach(machineForm -> {
                MachineDTO machineDTO = new MachineDTO();
                BeanUtils.copyProperties(machineForm, machineDTO);
                machineList.add(machineDTO);
            });
            clusterDTO.setMachineList(machineList);
        }
        log.info(clusterDTO.toString());
        clusterService.updateCluster(clusterDTO);
        return ResponseVo.ResponseBuilder.buildSuccess();
    }

    @GetMapping("/machine/{id}")
    public ResponseVo<List<MachineDTO>> getClusterMachines(@PathVariable("id") Integer id) {
        ClusterDTO clusterDTO = clusterService.getById(id);
        if (clusterDTO == null) {
            return ResponseVo.ResponseBuilder.buildFailed("数据不存在");
        }
        if (!CollectionUtils.isEmpty(clusterDTO.getMachineList())) {
            return ResponseVo.ResponseBuilder.buildSuccess(clusterDTO.getMachineList());
        }
        return ResponseVo.ResponseBuilder.buildSuccess(Lists.newArrayList());
    }
}
