package com.devops.repository;

import com.devops.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MachineRepository extends JpaRepository<Machine, Integer>, JpaSpecificationExecutor<Machine> {

}