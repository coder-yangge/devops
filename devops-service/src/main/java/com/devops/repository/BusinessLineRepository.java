package com.devops.repository;

import com.devops.entity.BusinessLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessLineRepository extends JpaRepository<BusinessLine, Integer>, JpaSpecificationExecutor<BusinessLine> {

}