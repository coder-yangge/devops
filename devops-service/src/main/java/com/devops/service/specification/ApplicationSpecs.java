package com.devops.service.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.devops.dto.ApplicationDto;
import com.devops.entity.Application;

public class ApplicationSpecs {
	
	public static Specification<Application> getPage(ApplicationDto applicationDto){
		return new Specification<Application>() {
			@Override
			public Predicate toPredicate(Root<Application> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicate = new ArrayList<>();
	            if (StringUtils.isNotBlank(applicationDto.getName())) {
	                predicate.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + applicationDto.getName() + "%"));
	            }
	            Predicate[] pre = new Predicate[predicate.size()];
	            return query.where(predicate.toArray(pre)).getRestriction();
			}
		};
		
		
	}
}
