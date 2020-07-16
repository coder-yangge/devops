package com.devops.web.controller;

import com.devops.common.constants.SytemProperties;
import com.devops.common.exception.BizException;
import com.devops.common.properties.GitProperties;
import com.devops.common.util.GitClient;
import com.devops.dto.ApplicationDto;
import com.devops.dto.EnvironmentDto;
import com.devops.dto.RepositoryDto;
import com.devops.service.ApplicationService;
import com.devops.service.EnvironmentService;
import com.devops.web.common.vo.ResponseVo;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yangge
 * @version 1.0.0
 * @title: BulidController
 * @date 2020/7/16 18:14
 */
@Controller
@RequestMapping("/build")
public class BuildController {

    @Autowired
    private EnvironmentService environmentService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private GitClient gitClient;

    @ResponseBody
    @GetMapping("/application/evn/{application}")
    public ResponseVo<EnvironmentDto> getEnvironment(@PathVariable("application") Integer applicationId) {
        ApplicationDto applicationDto = applicationService.findById(applicationId);
        if (applicationDto == null) {
            throw new BizException("该应用不存在");
        }
        EnvironmentDto environmentDto = applicationDto.getEnvironment();
        if (environmentDto == null) {
            throw new BizException("该应用构建信息不存在");
        }
        String appName = applicationDto.getName();
        File file = new File(gitClient.getLocalPath() + appName);
        if (!file.exists()) {
            throw new BizException("获取仓库代码失败");
        }
        return ResponseVo.ResponseBuilder.buildSuccess(environmentDto);
    }

    @ResponseBody
    @GetMapping("/application/branch/{application}")
    public ResponseVo<List<String>> getRemoteBranch(@PathVariable("application") Integer applicationId) {
        ApplicationDto applicationDto = applicationService.findById(applicationId);
        if (applicationDto == null) {
            throw new BizException("该应用不存在");
        }
        RepositoryDto repository = applicationDto.getRepository();
        if (repository == null) {
            throw new BizException("该应用构建信息不存在");
        }
        String appName = applicationDto.getName();
        GitProperties gitProperties = new GitProperties();
        gitProperties.setAppName(appName);
        gitProperties.setUserName(repository.getUserName());
        gitProperties.setPassWord(repository.getPassWord());
        try {
            List<String> remoteBranchList = gitClient.getRemoteBranchList(gitProperties);
            List<String> branchList = remoteBranchList.stream().map(branch -> branch.replace(SytemProperties.GIT_REMOTE_BRANCH_PRIX, ""))
                    .collect(Collectors.toList());
            return ResponseVo.ResponseBuilder.buildSuccess(branchList);
        } catch (GitAPIException e) {
            e.printStackTrace();
            throw new BizException("获取仓库代码失败");
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException("获取仓库代码失败");
        }
    }
}
