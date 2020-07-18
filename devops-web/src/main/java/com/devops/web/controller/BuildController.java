package com.devops.web.controller;

import com.devops.common.acount.Account;
import com.devops.common.acount.AccountContextHolder;
import com.devops.common.constants.SystemProperties;
import com.devops.common.exception.BizException;
import com.devops.common.properties.GitProperties;
import com.devops.common.util.GitClient;
import com.devops.dto.ApplicationDto;
import com.devops.dto.BuildDTO;
import com.devops.dto.EnvironmentDto;
import com.devops.dto.RepositoryDto;
import com.devops.event.CompileEvent;
import com.devops.service.ApplicationService;
import com.devops.service.EnvironmentService;
import com.devops.web.common.vo.ResponseVo;
import com.devops.web.form.BuildForm;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
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
@Slf4j
public class BuildController {

    @Autowired
    private EnvironmentService environmentService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private GitClient gitClient;

    @Autowired
    private ApplicationEventMulticaster eventMulticaster;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

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
            throw new BizException("该应用仓库信息不存在");
        }
        String appName = applicationDto.getName();
        GitProperties gitProperties = new GitProperties();
        gitProperties.setAppName(appName);
        gitProperties.setUserName(repository.getUserName());
        gitProperties.setPassWord(repository.getPassWord());
        try {
            List<String> remoteBranchList = gitClient.getRemoteBranchList(gitProperties);
            List<String> branchList = remoteBranchList.stream().map(branch -> branch.replace(SystemProperties.GIT_REMOTE_BRANCH_PRIX, ""))
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

    @GetMapping("/application/build/page")
    public String buildPage(BuildForm buildForm, ModelMap modelMap) {
        ApplicationDto applicationDto = applicationService.findById(buildForm.getApplicationId());
        modelMap.put("buildInfo", buildForm);
        modelMap.put("application", applicationDto);
        return "devops/build/build";
    }

    @ResponseBody
    @PostMapping("/application/build")
    public ResponseVo<String> build(@RequestBody BuildForm buildForm) {
        Integer applicationId = buildForm.getApplicationId();
        ApplicationDto applicationDto = applicationService.findById(applicationId);
        BuildDTO buildDTO = new BuildDTO();
        BeanUtils.copyProperties(buildForm, buildDTO);
        Account account = AccountContextHolder.getAccount();
        CompileEvent event = new CompileEvent(applicationDto, account, buildDTO);
        eventMulticaster.multicastEvent(event);
        log.info("发送消息user[{}] topic[{}] 内容[{}]", account.getUserName(), "/queue/compile/log", "test");
        messagingTemplate.convertAndSendToUser(account.getUserName(), "/queue/compile/log", "test");
        return ResponseVo.ResponseBuilder.buildFailed();
    }

}
