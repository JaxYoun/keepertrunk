package com.troy.keeper.fileupload.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.troy.keeper.fileupload.component.impl.SftpFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SshUtil {
    private static final Log log = LogFactory.getLog(SftpFileUpload.class);

    /**
     * 执行ssh命令
     * @param host
     * @param user
     * @param psw
     * @param port
     * @param command
     * @return
     */
    public static String exec(String host, String user, String psw, int port, String command) {
        String result = "";
        Session session = null;
        ChannelExec openChannel = null;
        try {
            JSch jsch = new JSch();
            // getSession()只是创建一个session，需要设置必要的认证信息之后，调用connect()才能建立连接。
            session = jsch.getSession(user, host, port);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword(psw);

            session.connect();

            // 调用openChannel(String type)
            // 可以在session上打开指定类型的channel。该channel只是被初始化，使用前需要先调用connect()进行连接。
            //   Channel的类型可以为如下类型：
            //   shell - ChannelShell
            //   exec - ChannelExec
            //   direct-tcpip - ChannelDirectTCPIP
            //   sftp - ChannelSftp
            //   subsystem - ChannelSubsystem
            // 其中，ChannelShell和ChannelExec比较类似，都可以作为执行Shell脚本的Channel类型。它们有一个比较重要的区别：ChannelShell可以看作是执行一个交互式的Shell，而ChannelExec是执行一个Shell脚本。
            openChannel = (ChannelExec) session.openChannel("exec");
            openChannel.setCommand(command);
            int exitStatus = openChannel.getExitStatus();
            log.info(exitStatus);
            openChannel.connect();

            InputStream in = openChannel.getInputStream();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in));
            String buf = null;
            while ((buf = reader.readLine()) != null) {
                result += " " + buf;
            }
        } catch (JSchException e) {
            result += e.getMessage();
        } catch (IOException e) {
            result += e.getMessage();
        } finally {
            if (openChannel != null && !openChannel.isClosed()) {
                openChannel.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
        return result;
    }
}
