package com.devops.repository;

import com.devops.entity.PackageRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PackageRecordRepository extends JpaRepository<PackageRecord, Integer>, JpaSpecificationExecutor<PackageRecord> {

}