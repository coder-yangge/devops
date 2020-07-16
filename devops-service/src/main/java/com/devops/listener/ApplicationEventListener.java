package com.devops.listener;

import com.devops.common.properties.GitProperties;
import com.devops.common.util.GitClient;
import com.devops.dto.RepositoryDto;
import com.devops.event.PullCodeEvent;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;

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
        GitProperties gitProperties = new GitProperties();
        gitProperties.setAppName(appName);
        gitProperties.setUserName(repository.getUserName());
        gitProperties.setPassWord(repository.getPassWord());
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

}
