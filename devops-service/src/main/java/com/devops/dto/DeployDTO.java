package com.devops.dto;

import com.devops.common.acount.Account;
import lombok.Data;

/**
 * @author yangge
 * @version 1.0.0
 * @title: DeployDTo
 * @date 2020/7/30 19:42
 */
@Data
public class DeployDTO {

    private ApplicationDto applicationDto;

    private String env;

    private Integer recordId;

    private String remark;

    private Account account;
}
