package com.devops.handler;

import com.devops.common.constants.SystemProperties;
import com.devops.common.ssh.SSHClient;
import com.devops.dto.ApplicationDto;
import com.devops.dto.DeployDTO;
import com.devops.dto.MachineDTO;
import com.devops.dto.PackageRecordDTO;
import com.devops.service.MachineService;
import com.devops.service.PackageRecordService;
import freemarker.template.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yangge
 * @version 1.0.0
 * @title: CommonDeployHandler
 * @date 2020/7/30 19:41
 */
@Service
@Slf4j
public class CommonDeployHandler implements DeployHandler {

    @Autowired
    private MachineService machineService;

    @Autowired
    private PackageRecordService packageRecordService;

    @Autowired
    private Configuration configuration;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Async("asyncCompileThreadPool")
    @Override
    public void deployApp(DeployDTO deployDTO) {
        String user = deployDTO.getAccount().getUserName();
        ApplicationDto applicationDto = deployDTO.getApplicationDto();
        Integer deployId = applicationDto.getEnvironment().getDeployId();
        PackageRecordDTO packageRecordDTO = packageRecordService.getById(deployDTO.getRecordId());
        if (ObjectUtils.isEmpty(packageRecordDTO)) {
            return;
        }
        MachineDTO machineDTO = machineService.getById(deployId);
        String ip = machineDTO.getIp();
        String sshUserName = machineDTO.getSshUserName();
        String sshPassword = machineDTO.getSshPassword();
        String filePath = packageRecordDTO.getFilePath();
        String packagePath = applicationDto.getEnvironment().getPackagePath();
        String fileName = packagePath.substring(packagePath.lastIndexOf("/") + 1, packagePath.length());
        log.info("机器【{}】开始部署应用【{}】", ip, applicationDto.getName());
        log.info("机器【{}】准备上传部署包【{}】,本地部署包路径【{}】",ip, fileName, filePath);
        pushDeployMessage(user, message(1, ip, applicationDto.getName()));
        pushDeployMessage(user, message(2, ip, fileName, filePath));
        int serverAliveInterval = 5 * 1000;
        int timeout = 10 * 1000;
        File file = new File(filePath);
        if (!file.exists()) {
            log.info("本地部署包路径【{}】不存在，停止部署", filePath);
            return;
        }
        SSHClient sshClient = new SSHClient(ip, sshUserName, sshPassword, timeout, serverAliveInterval);
        try {
            // 上传至目标服务器
            sshClient.upload(SystemProperties.FILE_PATH + sshUserName, fileName, filePath, null);
            log.info("机器【{}】部署包【{}】上传成功", ip, filePath);
            pushDeployMessage(user, message(3, ip, filePath));
            // 执行启动脚本
            pushDeployMessage(user, message(4, ip, applicationDto.getEnvironment().getStartScript()));
            log.info("机器【{}】准备上传启动脚本【{}】", ip, applicationDto.getEnvironment().getStartScript());
            Template template = configuration.getTemplate("/script/linux/start_script.ftl");
            Map<String, Object> model = new HashMap<>();
            model.put("appName", applicationDto.getName());
            model.put("appTar", file.getName());
            model.put("deployPath", applicationDto.getEnvironment().getDeployPath());
            String startScript = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            log.error(startScript);
            pushDeployMessage(user, startScript);
            // InputStream inputStream = new ByteArrayInputStream(applicationDto.getEnvironment().getStartScript().getBytes());
            InputStream inputStream = new ByteArrayInputStream(startScript.getBytes());
            sshClient.upload(SystemProperties.FILE_PATH + sshUserName, applicationDto.getName() + ".bash", inputStream, null);
            log.info("机器【{}】启动脚本上传成功", ip);
            pushDeployMessage(user, message(5, ip));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            log.info("机器【{}】开始执行启动脚本", ip);
            pushDeployMessage(user, message(6, ip));
            StringBuilder command = new StringBuilder("bash ").append(applicationDto.getName()).append(".bash");
            sshClient.executeCommand(printStream, command.toString(), false);
            log.info(outputStream.toString());
            pushDeployMessage(user, outputStream.toString());
            log.info("机器【{}】启动脚本执行完毕", ip);
            pushDeployMessage(user, message(7, ip));
        } catch (InterruptedException e) {
            log.info("机器【{}】启动脚本执行失败", ip);
            e.printStackTrace();
        } catch (TemplateNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            // TODO 删除启动脚本

        }
    }

    private void pushDeployMessage(String user, String message) {
        messagingTemplate.convertAndSendToUser(user, SystemProperties.DEPLOY_LOG_TOPIC, message);
    }

    private String message(int index, String... args) {
        return String.format(messages.get(index), args);
    }

    static Map<Integer, String> messages = new HashMap<>();

    static {
        messages.put(1, "机器【%s】开始部署应用【%s】");
        messages.put(2, "机器【%s】准备上传部署包【%s】,本地部署包路径【%s】");
        messages.put(3, "机器【%s】部署包【%s】上传成功");
        messages.put(4, "机器【%s】准备上传启动脚本【%s】");
        messages.put(5, "机器【%s】启动脚本上传成功");
        messages.put(6, "机器【%s】开始执行启动脚本");
        messages.put(7, "机器【%s】启动脚本执行完毕");
    }
}
