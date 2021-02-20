package com.devops.service.specification;

import com.devops.dto.MachineDTO;
import com.devops.entity.Machine;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yangge
 * @version 1.0.0
 * @description: TODO
 * @date 2020/8/5 14:01
 */
public class MachineSpces {

    public static Specification<Machine> getPage(MachineDTO machineDTO){
        return (Specification<Machine>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicate = new ArrayList<>();
            if (StringUtils.isNotBlank(machineDTO.getName())) {
                predicate.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + machineDTO.getName() + "%"));
            }
            if (ObjectUtils.isNotEmpty(machineDTO.getIp())) {
                predicate.add(criteriaBuilder.equal(root.get("ip"), machineDTO.getIp()));
            }
            Predicate[] pre = new Predicate[predicate.size()];
            return query.where(predicate.toArray(pre)).getRestriction();
        };
    }
}
