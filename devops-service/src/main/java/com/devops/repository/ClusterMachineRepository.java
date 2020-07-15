package com.devops.repository;

import com.devops.entity.ClusterMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ClusterMachineRepository extends JpaRepository<ClusterMachine, Integer>, JpaSpecificationExecutor<ClusterMachine> {

    List<ClusterMachine> getAllByClusterId(Integer clusterId);
}