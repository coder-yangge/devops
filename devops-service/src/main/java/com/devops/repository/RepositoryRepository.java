package com.devops.repository;

import com.devops.entity.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositoryRepository extends JpaRepository<Repository, Integer>, JpaSpecificationExecutor<Repository> {

    int deleteByApplicationId(Integer applicationId);
}