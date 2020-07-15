package com.devops.repository;

import com.devops.entity.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EnvironmentRepository extends JpaRepository<Environment, Integer>, JpaSpecificationExecutor<Environment> {

    int deleteByApplicationId(Integer applicationId);

    Environment findByApplicationId(Integer applicationId);
}