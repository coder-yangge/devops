package com.devops.service;

import com.devops.common.dto.PageDTO;
import com.devops.dto.MachineDTO;

import java.util.List;

/**
 * @author yangge
 * @version 1.0.0
 * @title: MachineService
 * @date 2020/7/14 18:29
 */
public interface MachineService {

    void save(MachineDTO machineDTO);

    PageDTO<MachineDTO> getByPage(MachineDTO machineDTO, PageDTO pageDTO);

    MachineDTO getById(Integer id);

    List<MachineDTO> getMachineList();

    void delete(Integer id);
}
