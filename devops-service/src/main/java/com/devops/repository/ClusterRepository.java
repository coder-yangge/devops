package com.devops.repository;

import com.devops.entity.Cluster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClusterRepository extends JpaRepository<Cluster, Integer>, JpaSpecificationExecutor<Cluster> {

}