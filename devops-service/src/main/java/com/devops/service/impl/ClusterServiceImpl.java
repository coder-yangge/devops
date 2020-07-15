package com.devops.service.impl;

import com.devops.common.dto.PageDTO;
import com.devops.dto.ClusterDTO;
import com.devops.dto.MachineDTO;
import com.devops.entity.Cluster;
import com.devops.entity.ClusterMachine;
import com.devops.repository.ClusterMachineRepository;
import com.devops.repository.ClusterRepository;
import com.devops.repository.MachineRepository;
import com.devops.service.ClusterService;
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
                        BeanUtils.copyProperties(machine, machineDTO);
                        machineDTOList.add(machineDTO);
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
}
