package com.devops.common.util;

import com.devops.common.properties.GitProperties;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yangge
 * @version 1.0.0
 * @title: GitClient
 * @date 2020/7/16 13:40
 */
@Slf4j
@Component
public class GitClient {

    private static String localPath;

    public boolean clone(String url, GitProperties gitProperty) throws GitAPIException {
        Git git = Git.cloneRepository().setURI(url)
                .setCredentialsProvider(provider(gitProperty))
                .setDirectory(new File(localPath + gitProperty.getAppName()))
                .setCloneAllBranches(true).call();
        git.close();
        return Boolean.TRUE;
    }

    public boolean pull(GitProperties properties) throws IOException, GitAPIException {

        PullResult call = Git.open(new File(localPath + properties.getAppName())).pull()
                .setCredentialsProvider(provider(properties)).call();
        return call.isSuccessful();
    }

    public PullResult pullLog(GitProperties properties) throws IOException, GitAPIException {

        PullResult call = Git.open(new File(localPath + properties.getAppName())).pull()
                .setCredentialsProvider(provider(properties)).call();
        return call;
    }

    public boolean checkOutBranch(GitProperties properties, String branch) throws IOException, GitAPIException {
        Git git = Git.open(new File(localPath + properties.getAppName()));
        String localBranch = git.getRepository().getBranch();
        if (branch.equals(localBranch)) {
            return Boolean.TRUE;
        }
        List<Ref> refList = git.branchList().call();
        boolean match = refList.stream().anyMatch(ref -> ref.getName().endsWith(branch));
        if (match) {
            // 分支已存在，切换
            git.checkout().setName(branch).call();
            git.close();
            return Boolean.TRUE;
        } else {
            // 分支不存在，创建
            git.checkout().setCreateBranch(true).setName(branch).call();
            PullResult result = git.pull().setCredentialsProvider(provider(properties)).call();
            git.close();
            return result.isSuccessful();
        }
    }

    public List<String> getLocalBranchList(GitProperties properties) throws GitAPIException, IOException {
        Git git = Git.open(new File(localPath + properties.getAppName()));
        List<Ref> refList = git.branchList().call();
        List<String> list = refList.stream().map(Ref::getName).collect(Collectors.toList());
        git.close();
        return list;
    }

    public List<String> getRemoteBranchList(GitProperties properties) throws GitAPIException, IOException {
        if (!pull(properties)) {
            return Collections.emptyList();
        }
        Git git = Git.open(new File(localPath + properties.getAppName()));
        git.close();
        List<Ref> refList = git.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call();
        List<String> list = refList.stream().map(Ref::getName).collect(Collectors.toList());
        return list;
    }

    private UsernamePasswordCredentialsProvider provider(GitProperties properties) {
        return new UsernamePasswordCredentialsProvider(properties.getUserName(), properties.getPassWord());
    }

    public String getLocalPath() {
        return localPath;
    }

    @Value("${application.clone.location}")
    public void setLocalPath(String localPath) {
        GitClient.localPath = localPath;
    }

    public static void main(String[] args) {
        GitClient.localPath = "D:/develop/software/JAVA/Iproject/application/";
        GitProperties gitProperties = new GitProperties();
        gitProperties.setAppName("demo");
        gitProperties.setUserName("18740448749@163.com");
        gitProperties.setPassWord("lh@199396");
        GitClient client = new GitClient();
        try {
            //client.clone("https://github.com/coder-yangge/demo-jenkins.git", gitProperties);
            //List<String> branchList = client.getLocalBranchList(gitProperties);
           //  System.out.println(branchList);
            //client.checkOutBranch(gitProperties, "master");
            System.out.println(client.getRemoteBranchList(gitProperties));
        } catch (GitAPIException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
