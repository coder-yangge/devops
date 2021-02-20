package com.devops.common.constants;

/**
 * @author yangge
 * @version 1.0.0
 * @title: SytemProperties
 * @description: 系统常量
 * @date 2020/7/13 15:53
 */
public class SystemProperties {

    public static final String LINE =  System.getProperty("line.separator");

    // public static final String FILE_PATH = System.getProperty("file.separator");

    public static final String FILE_PATH = "/";

    public static final String GIT_REMOTE_BRANCH_PRIX = "refs/remotes/origin/";

    public static final String COMPILE_LOG_TOPIC = "/queue/compile/log";

    public static final String DEPLOY_LOG_TOPIC = "/queue/deploy/log";

    public static String OS;

    static {
        OS = System.getProperty("os.name").toLowerCase();
    }

}
