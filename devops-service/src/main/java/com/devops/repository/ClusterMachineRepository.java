package com.devops.repository;

import com.devops.entity.ClusterMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClusterMachineRepository extends JpaRepository<ClusterMachine, Integer>, JpaSpecificationExecutor<ClusterMachine> {

}