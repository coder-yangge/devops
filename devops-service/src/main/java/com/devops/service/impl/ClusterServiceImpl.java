package com.devops.service.impl;

import com.devops.common.dto.PageDTO;
import com.devops.common.exception.BizException;
import com.devops.dto.ClusterDTO;
import com.devops.dto.MachineDTO;
import com.devops.entity.Cluster;
import com.devops.entity.ClusterMachine;
import com.devops.entity.Machine;
import com.devops.repository.ClusterMachineRepository;
import com.devops.repository.ClusterRepository;
import com.devops.repository.MachineRepository;
import com.devops.service.ClusterService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yangge
 * @version 1.0.0
 * @title: ClusterServiceImpl
 * @date 2020/7/14 18:44
 */
@Service
public class ClusterServiceImpl implements ClusterService {

    @Autowired
    private ClusterRepository clusterRepository;

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private ClusterMachineRepository clusterMachineRepository;

    @Override
    public PageDTO<ClusterDTO> getPage(ClusterDTO clusterDTO, PageDTO pageDTO) {
        Cluster cluster = new Cluster();
        if (StringUtils.isNotBlank(clusterDTO.getName())) {
            cluster.setName(clusterDTO.getName());
        }
        Pageable pageable = PageRequest.of(pageDTO.getPageNum() -1, pageDTO.getPageSize());
        Page<Cluster> clusterPage = clusterRepository.findAll(Example.of(cluster), pageable);
        List<Cluster> content = clusterPage.getContent();
        List<ClusterDTO> clusterDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(content)) {
            content.forEach(e -> {
                ClusterDTO dto = new ClusterDTO();
                BeanUtils.copyProperties(e, dto);
                List<ClusterMachine> machines = clusterMachineRepository.getAllByClusterId(e.getId());
                List<MachineDTO> machineDTOList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(machines)) {
                    machines.forEach( machine ->{
                        MachineDTO machineDTO = new MachineDTO();
                        Optional<Machine> optional = machineRepository.findById(machine.getMachineId());
                        if (optional.isPresent()) {
                            BeanUtils.copyProperties(optional.get(), machineDTO);
                            machineDTOList.add(machineDTO);
                        }
                    });
                }
                dto.setMachineList(machineDTOList);
                clusterDTOList.add(dto);
            });
        }
        return PageDTO.of(clusterPage, clusterDTOList);
    }

    public List<ClusterDTO> getClusterList() {
        List<Cluster> clusterList = clusterRepository.findAll();
        List<ClusterDTO> dtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(clusterList)) {
            clusterList.forEach(cluster -> {
                ClusterDTO dto = new ClusterDTO();
                BeanUtils.copyProperties(cluster, dto);
                dtoList.add(dto);
            });
        }
        return dtoList;
    }

    @Override
    public void save(ClusterDTO clusterDTO) {
        Cluster cluster = new Cluster();
        BeanUtils.copyProperties(clusterDTO, cluster);
        Integer clusterId = clusterRepository.save(cluster).getId();
        if (!CollectionUtils.isEmpty(clusterDTO.getMachineList())) {
            clusterDTO.getMachineList().forEach(machineDTO -> {
                ClusterMachine clusterMachine = new ClusterMachine();
                clusterMachine.setClusterId(clusterId);
                clusterMachine.setMachineId(machineDTO.getId());
                clusterMachineRepository.save(clusterMachine);
            });
        }
    }

    @Override
    public void delete(Integer id) {
        if (!clusterRepository.findById(id).isPresent()) {
           throw new BizException("该集群已被删除");
        }
        clusterRepository.deleteById(id);
        // 删除对应关联的机器
        clusterMachineRepository.deleteByClusterId(id);
    }

    @Override
    public ClusterDTO getById(Integer id) {
        Optional<Cluster> cluster = clusterRepository.findById(id);
        ClusterDTO clusterDTO = new ClusterDTO();
        if (cluster.isPresent()) {
            BeanUtils.copyProperties(cluster.get(), clusterDTO);
            ClusterMachine condition = new ClusterMachine();
            condition.setClusterId(id);
            List<ClusterMachine> machineList = clusterMachineRepository.findAll(Example.of(condition));
            if (!CollectionUtils.isEmpty(machineList)) {
                List<MachineDTO> machineDTOList = new ArrayList<>();
                List<Integer> machineIds = machineList.stream().map(ClusterMachine::getMachineId).collect(Collectors.toList());
                List<Machine> machines = machineRepository.findAllById(machineIds);
                if (!CollectionUtils.isEmpty(machines)) {
                    machines.forEach(machine -> {
                        MachineDTO machineDTO = new MachineDTO();
                        BeanUtils.copyProperties(machine, machineDTO);
                        machineDTOList.add(machineDTO);
                    });
                }
                clusterDTO.setMachineList(machineDTOList);
            }
            return clusterDTO;
        }
        return null;
    }

    @Override
    public void updateCluster(ClusterDTO clusterDTO) {
        Cluster cluster = new Cluster();
        BeanUtils.copyProperties(clusterDTO, cluster);
        Integer clusterId = clusterRepository.save(cluster).getId();
        ClusterMachine condition = new ClusterMachine();
        condition.setClusterId(clusterId);
        List<ClusterMachine> existMachineList = clusterMachineRepository.findAll(Example.of(condition));
        List<MachineDTO> machineList = clusterDTO.getMachineList();
        // 取交集 为已存在的数据
        // 取差集 为需要判断的数据，要么删除，要么新增
        if (CollectionUtils.isEmpty(machineList)) {
            if (!existMachineList.isEmpty()) {
                clusterMachineRepository.deleteAll(existMachineList);
            }
        } else {
            if (existMachineList.isEmpty()) {
                saveMachines(machineList, clusterId);
            } else {
                List<MachineDTO> updateMachines = Lists.newArrayList();
                for (int i = 0; i < existMachineList.size(); i++) {
                    boolean flag = false;
                    for (int j = 0; j < machineList.size(); j++) {
                        if (existMachineList.get(i).getMachineId().equals(machineList.get(j).getId())) {
                            flag = true;
                            MachineDTO remove = machineList.remove(j);
                            updateMachines.add(remove);
                            j--;
                            break;
                        }
                    }
                    if (flag) {
                        existMachineList.remove(i);
                        i--;
                    }
                }
                // existMachineList 剩下的数据为需要删除的数据
                // List<Integer> deleteMachines = existMachineList.stream().map(ClusterMachine::getId).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(machineList)) {
                    saveMachines(machineList, clusterId);
                }

                if (!CollectionUtils.isEmpty(existMachineList)) {
                    clusterMachineRepository.deleteAll(existMachineList);
                }
            }
        }
    }

    private void saveMachines(List<MachineDTO> machineDTOS, Integer clusterId) {
        machineDTOS.forEach(e ->{
            ClusterMachine clusterMachine = new ClusterMachine();
            clusterMachine.setClusterId(clusterId);
            clusterMachine.setMachineId(e.getId());
            clusterMachineRepository.save(clusterMachine);
        });
    }
}
