package com.devops.web.listener;

import com.devops.common.acount.Account;
import com.devops.common.constants.SystemProperties;
import com.devops.common.enums.BuildStatusEnum;
import com.devops.common.enums.SystemOS;
import com.devops.common.properties.GitProperties;
import com.devops.common.util.GitClient;
import com.devops.dto.ApplicationDto;
import com.devops.dto.BuildDTO;
import com.devops.dto.EnvironmentDto;
import com.devops.dto.RepositoryDto;
import com.devops.entity.PackageRecord;
import com.devops.event.CompileEvent;
import com.devops.event.PullCodeEvent;
import com.devops.repository.PackageRecordRepository;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yangge
 * @version 1.0.0
 * @title: ApplicationEventListener
 * @date 2020/7/16 17:22
 */
@Component
@Slf4j
public class ApplicationEventListener {

    @Autowired
    private GitClient gitClient;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private FreeMarkerConfigurer freemarker;

    @Autowired
    private PackageRecordRepository recordRepository;

    @Value("${application.package.save-location}")
    private String packageSaveLocation;

    @Async
    @EventListener
    public void cloneRepositoryEvent(PullCodeEvent event) {
        String appName = event.getApplicationDto().getName();
        RepositoryDto repository = event.getApplicationDto().getRepository();
        String localPath = gitClient.getLocalPath();
        File file = new File(localPath + appName);
        if (file.exists()) {
            file.delete();
        }
        GitProperties gitProperties = create(appName, repository.getUserName(), repository.getPassWord());
        try {
            boolean flag = gitClient.clone(repository.getAddress(), gitProperties);
            if (!flag) {
                log.error("获取应用[{}]远程仓库[{}]代码失败", appName, repository.getAddress());
            }
        } catch (GitAPIException e) {
            log.error("获取应用[{}]远程仓库[{}]代码失败", appName, repository.getAddress());
            e.printStackTrace();
        }
    }

    /**
     * 构建事件
     * TODO 应用构建时，为防止多个用户构建同一个应用，此处应该添加分布式锁，当获取锁失败时，直接提示构建失败
     * @param event CompileEvent
     */
    @Async("asyncServiceExecutor")
    @EventListener
    public void compileEvent(CompileEvent event) {
        log.info("开始构建");
        Account account = event.getAccount();
        String appName = event.getApplicationDto().getName();
        RepositoryDto repository = event.getApplicationDto().getRepository();
        BuildDTO buildDTO = event.getBuildDTO();
        EnvironmentDto environment = event.getApplicationDto().getEnvironment();
        pushCompileLog(account.getUserName(), "开始构建.....");
        GitProperties gitProperties = create(appName, repository.getUserName(), repository.getPassWord());
        String os = SystemProperties.OS;
        SystemOS systemOS = SystemOS.get(os);
        if (ObjectUtils.isEmpty(systemOS)) {
            pushCompileLog(account.getUserName(), "暂不支持当前操作系统");
        }
        String exception = null;
        boolean success = false;
        try {
            pushCompileLog(account.getUserName(), "开始切换分支" + buildDTO.getBranch());
            gitClient.checkOutBranch(gitProperties, buildDTO.getBranch());
            pushCompileLog(account.getUserName(), "开始从远程仓库拉取.....");
            boolean pull = gitClient.pull(gitProperties);
            pushCompileLog(account.getUserName(), "拉取" + pull);
            String localPath = gitClient.getLocalPath();
            File file = new File(localPath + appName);
            if (file.exists()) {
                Map<String, Object> data = new HashMap<>();
                data.put("filePath", file.getAbsolutePath());
                data.put("packageCommand", environment.getPackageCommand());
                data.put("packageArg", environment.getPackageArg());
                String templateName = "/script/" + systemOS.getCode() + "/compile.ftl";
                Template template = freemarker.getConfiguration().getTemplate(templateName);
                String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
                pushCompileLog(account.getUserName(), content);
                Process ps = Runtime.getRuntime().exec(content);
                sendLog(ps.getInputStream(), SystemProperties.COMPILE_LOG_TOPIC, account.getUserName());
                ps.waitFor();
                int i = ps.exitValue();
                if (i == 0) {
                    log.info("执行完成.");
                } else {
                    log.info("执行失败.");
                }
                ps.destroy();
                ps = null;
                success = true;
            } else {
                pushCompileLog(account.getUserName(), "远程获取失败");
            }
        } catch (IOException e) {
            exception = "IO异常" + e.getMessage();
            e.printStackTrace();
        } catch (GitAPIException e) {
            exception = "获取远程仓库代码异常" + e.getMessage();
            e.printStackTrace();
        } catch (TemplateException e) {
            exception = "命令模板转换异常" + e.getMessage();
            e.printStackTrace();
        } catch (InterruptedException e) {
            exception = "系统异常" + e.getMessage();
            e.printStackTrace();
        }
        if (!StringUtils.isEmpty(exception)) {
            pushCompileLog(account.getUserName(), exception);
            pushCompileLog(account.getUserName(), "构建失败");
        }
        pushCompileLog(account.getUserName(), "构建成功");
        // 保存构建记录
        try {
            this.savePackageRecord(event, success);
        } catch (IOException e) {
            pushCompileLog(account.getUserName(), "保存构建记录失败");
            log.error("保存构建记录失败：{}", e.getMessage());
        }
    }

    private void pushCompileLog(String user, String message) {
        messagingTemplate.convertAndSendToUser(user, SystemProperties.COMPILE_LOG_TOPIC, message);
    }

    public void sendLog(InputStream inputStream, String topic, String user) throws IOException {
        String line = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
        while ((line = reader.readLine()) != null) {
            messagingTemplate.convertAndSendToUser(user, topic, line);
        }
        reader.close();
    }

    private GitProperties create(String appName, String userName, String passWord) {
        GitProperties gitProperties = new GitProperties();
        gitProperties.setAppName(appName);
        gitProperties.setUserName(userName);
        gitProperties.setPassWord(passWord);
        return gitProperties;
    }

    private void savePackageRecord(CompileEvent event, boolean success) throws IOException {
        ApplicationDto applicationDto = event.getApplicationDto();
        BuildDTO buildDTO = event.getBuildDTO();
        EnvironmentDto environment = applicationDto.getEnvironment();
        PackageRecord record = new PackageRecord();
        record.setApplicationId(buildDTO.getApplicationId());
        record.setBranch(buildDTO.getBranch());
        record.setName(buildDTO.getRemark());
        record.setCreateDate(new Date());
        record.setStatus(BuildStatusEnum.FAILED.getCode());
        record.setCreateUser(event.getAccount().getUserName());
        String packagePath = environment.getPackagePath();
        String localPath = gitClient.getLocalPath() + applicationDto.getName();
        if (!packagePath.startsWith(SystemProperties.FILE_PATH)) {
            packagePath = SystemProperties.FILE_PATH + packagePath ;
        }
        if (success) {
            File file = new File(localPath + packagePath);
            log.info("{} package file path: {}", applicationDto.getName(), file.getAbsolutePath());
            FileInputStream inputStream = new FileInputStream(file);
            String md5Hex = DigestUtils.md5Hex(inputStream);
            record.setVersion(md5Hex);
            record.setStatus(BuildStatusEnum.SUCCESS.getCode());
            inputStream.close();
            String saveLocation = packageSaveLocation + SystemProperties.FILE_PATH + applicationDto.getName() + SystemProperties.FILE_PATH + md5Hex;
            File copyFile = new File(saveLocation);
            if (!copyFile.exists()) {
                copyFile.mkdirs();
            }
            FileOutputStream outputStream = new FileOutputStream(saveLocation + SystemProperties.FILE_PATH + file.getName());
            FileInputStream read = new FileInputStream(file);
            IOUtils.copy(read, outputStream);
            read.close();
            outputStream.close();
            record.setFilePath(saveLocation + SystemProperties.FILE_PATH + file.getName());
        }
        recordRepository.save(record);
    }
}
