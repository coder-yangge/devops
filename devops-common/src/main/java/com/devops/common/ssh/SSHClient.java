package com.devops.common.ssh;

import com.devops.common.constants.SystemProperties;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * @author yangge
 * @version 1.0.0
 * @title: SSHClient
 * @date 2020/7/13 11:09
 */
@Slf4j
public class SSHClient {

    String hostname;

    String username;

    int port = 22;

    String password;

    int serverAliveInterval = 0;

    int timeout = 0;

    Boolean pty = Boolean.FALSE;


    public SSHClient(String hostname, String username, String password, int timeout, int serverAliveInterval) {
        this.hostname = hostname;
        this.username = username;
        this.password = password;
        this.timeout = timeout;
        this.serverAliveInterval = serverAliveInterval;
    }


    public void closeSession(final Session session, final ChannelExec channel, final ChannelSftp channelSftp) {
        if (channel != null) {
            channel.disconnect();
        }
        if (channelSftp != null) {
            channelSftp.disconnect();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

    private ChannelExec createChannel(final PrintStream logger, final Session session) throws JSchException {
        final ChannelExec channel = (ChannelExec) session.openChannel("exec");
        channel.setOutputStream(logger, true);
        channel.setExtOutputStream(logger, true);
        channel.setInputStream(null);
        if (pty == null) {
            pty = Boolean.FALSE;
        }
        channel.setPty(pty);

        return channel;
    }

    private ChannelSftp createSftpChannel(final Session session) throws JSchException {
        final ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();
        return channel;
    }

    private Session createSession() throws JSchException, IOException, InterruptedException {
        JSch jSch = new JSch();
        final Session session = jSch.getSession(username, hostname, port);
        session.setPassword(password);
        session.setServerAliveInterval(serverAliveInterval);
        final Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect(timeout);
        return session;
    }


    public int executeCommand(PrintStream logger, String command, boolean execEachLine) throws InterruptedException {
        Session session = null;
        int status = -1;
        try {
            session = createSession();
            if (execEachLine) {
                StringTokenizer commands = new StringTokenizer(command,"\n\r\f");
                while (commands.hasMoreTokens()) {
                    int i = doExecCommand(session, logger, commands.nextToken().trim());
                    if (i != 0) {
                        status = i;
                        break;
                    }
                    //if there are no more commands to execute return the status of the last command
                    if (!commands.hasMoreTokens()) {
                        status = i;
                    }
                }
            }
            else {
                status = doExecCommand(session, logger, command);
            }
            logger.printf("%n[SSH] completed");
            logger.printf("%n[SSH] exit-status: " + status + "%n%n");
        } catch (JSchException e) {
            logger.println("[SSH] Exception:" + e.getMessage());
            e.printStackTrace(logger);
        } catch (IOException e) {
            logger.println("[SSH] Exception:" + e.getMessage());
            e.printStackTrace(logger);
        } finally {
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
        return status;
    }

    private int doExecCommand(Session session, PrintStream logger, String command) throws InterruptedException, IOException, JSchException {
        ChannelExec channel = null;
        int status = -1;
        try {
            channel = createChannel(logger, session);
            channel.setCommand(command);
            InputStream in = channel.getInputStream();
            channel.connect();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0)
                        break;
                    logger.write(tmp, 0, i);
                }
                if (channel.isClosed()) {
                    status = channel.getExitStatus();
                    break;
                }
                logger.flush();
                Thread.sleep(1000);
            }
        } catch (JSchException e) {
            throw e;
        } finally {
            if (channel != null && channel.isConnected()) {
                channel.disconnect();
            }
        }
        return status;
    }

    public void upload(String remoteDir, String fileName, String filePath, PrintStream printStream) throws InterruptedException, JSchException, IOException, SftpException {
        Session session = null;
        session = createSession();

        ChannelSftp sftpChannel = createSftpChannel(session);
        String dst = remoteDir + SystemProperties.FILE_PATH + fileName;
        String src = filePath + SystemProperties.FILE_PATH + fileName;
        if (!exist(remoteDir, sftpChannel)) {
            log.error("目标服务器路径[{}]不存在", remoteDir);
        }
        log.info("开始上传，本地服务器路径：[{}]目标服务器路径：[{}]", src, dst);
        sftpChannel.put(src, dst);
        sftpChannel.disconnect();
        sftpChannel.exit();
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

    private boolean exist(String path, ChannelSftp sftp){
        boolean exist = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(path);
            exist = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                exist = false;
            }
        }
        return exist;

    }


    public void testConnection(final PrintStream logger) throws JSchException, IOException, InterruptedException {
        final Session session = createSession();
        closeSession( session,null, null);
    }

    public static void main(String[] args) throws InterruptedException, SftpException, JSchException, IOException {
        String hostname = "106.15.44.220";
        String username = "root";
        String password = "Lh199396";
        int serverAliveInterval = 5* 1000;
        int timeout = 10 * 1000;

        SSHClient sshClient = new SSHClient(hostname, username, password, timeout, serverAliveInterval);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        try {
            //sshClient.testConnection(printStream);System.getProperty("line.separator")
            String line = System.getProperty("line.separator");
            StringBuilder command = new StringBuilder("pwd && cd ../home/ && pwd");
            sshClient.executeCommand(printStream, command.toString(), false);
            System.out.println(outputStream.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sshClient.upload("/home/test", "壁纸1.jpg", "D:\\娱乐\\壁纸", null);

        System.out.println("success");
    }
}
