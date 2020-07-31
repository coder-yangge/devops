package com.devops.web.controller;

import com.devops.common.constants.SystemProperties;
import com.devops.common.enums.DeployTypeEnum;
import com.devops.common.exception.BizException;
import com.devops.common.ssh.SSHClient;
import com.devops.dto.ApplicationDto;
import com.devops.dto.DeployDTO;
import com.devops.dto.MachineDTO;
import com.devops.handler.DeployHandler;
import com.devops.service.ApplicationService;
import com.devops.service.MachineService;
import com.devops.service.PackageRecordService;
import com.devops.web.common.vo.ResponseVo;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author yangge
 * @version 1.0.0
 * @title: DeployController
 * @date 2020/7/29 22:21
 */
@RestController
@RequestMapping("/deploy")
public class DeployController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private PackageRecordService packageRecordService;

    @Autowired
    private DeployHandler deployHandler;

    @PostMapping("/{applicationId}/{env}/{packageRecordId}")
    public ResponseVo deploy(@PathVariable("applicationId") Integer applicationId,
                             @PathVariable("env") String env,
                             @PathVariable("packageRecordId") Integer packageRecordId) {
        // TODO 根据环境查询对用的应用信息
        ApplicationDto applicationDto = applicationService.findById(applicationId);
        if (ObjectUtils.isEmpty(applicationDto)) {
            throw new BizException("应用不存在");
        }
        // TODO 参数校验
        String deployPath = applicationDto.getEnvironment().getDeployPath();
        DeployTypeEnum deployTypeEnum = DeployTypeEnum.get(applicationDto.getEnvironment().getDeployType());
        DeployDTO deployDTO = new DeployDTO();
        deployDTO.setApplicationDto(applicationDto);
        deployDTO.setRecordId(packageRecordId);
        deployDTO.setEnv(env);
        switch (deployTypeEnum) {
            case MACHINE:
                deployHandler.deployApp(deployDTO);
                break;
            case CLUSTER:
                break;
            default:
                break;
        }
        return ResponseVo.ResponseBuilder.buildSuccess();
    }
}
