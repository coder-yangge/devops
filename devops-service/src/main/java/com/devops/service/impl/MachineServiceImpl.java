package com.devops.service.impl;

import com.devops.common.dto.PageDTO;
import com.devops.dto.MachineDTO;
import com.devops.entity.Machine;
import com.devops.repository.MachineRepository;
import com.devops.service.MachineService;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author yangge
 * @version 1.0.0
 * @title: MachineServiceImpl
 * @date 2020/7/14 18:29
 */
@Service
public class MachineServiceImpl implements MachineService {

    @Autowired
    private MachineRepository machineRepository;

    @Override
    public void save(MachineDTO machineDTO) {
        Machine machine = new Machine();
        BeanUtils.copyProperties(machineDTO, machine);
        machineRepository.save(machine);
    }

    @Override
    public PageDTO<MachineDTO> getByPage(MachineDTO machineDTO, PageDTO pageDTO) {
        Machine query = new Machine();
        if (StringUtils.isNotBlank(machineDTO.getName())) {
            query.setName(machineDTO.getName());
        }
        if (StringUtils.isNotBlank(machineDTO.getIp())) {
            query.setIp(machineDTO.getIp());
        }
        Pageable pageable = PageRequest.of(pageDTO.getPageNum() -1, pageDTO.getPageSize());
        Page<Machine> machinePage = machineRepository.findAll(Example.of(query), pageable);
        return PageDTO.of(machinePage, MachineDTO::new, BeanUtils::copyProperties);
    }

    @Override
    public MachineDTO getById(Integer id) {
        Optional<Machine> optional = machineRepository.findById(id);
        if (optional.isPresent()) {
            MachineDTO machineDTO = new MachineDTO();
            BeanUtils.copyProperties(optional.get(), machineDTO);
            return machineDTO;
        }
        return null;
    }

    @Override
    public List<MachineDTO> getMachineList() {
        List<Machine> machineList = machineRepository.findAll();
        if (CollectionUtils.isEmpty(machineList)) {
            return Collections.EMPTY_LIST;
        }
        List<MachineDTO> machineDTOList = new ArrayList<>();
        machineList.forEach(e ->{
            MachineDTO machineDTO = new MachineDTO();
            BeanUtils.copyProperties(e, machineDTO);
            machineDTOList.add(machineDTO);
        });
        return machineDTOList;
    }
}
