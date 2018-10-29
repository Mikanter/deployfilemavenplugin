package sample.plugin;

import com.jcraft.jsch.*;
import org.apache.maven.plugin.MojoExecutionException;
import com.google.common.io.CharStreams;

import java.io.*;

public class SshRun {
    private JSch ssh;
    private Session session;

    public void connect(String host, String user, String password) throws MojoExecutionException {
        try {
            ssh = new JSch();
            session = ssh.getSession(user, host);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect(15000);
            session.setServerAliveInterval(15000);
            if (!session.isConnected()) {
                throw new MojoExecutionException("Cannot establish connection to " + host);
            }
        } catch (JSchException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    public String exec(String command) throws MojoExecutionException {
        ChannelExec channel = null;
        try {
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            InputStream output = channel.getInputStream();
            channel.connect(15000);
            String result = CharStreams.toString(new InputStreamReader(output));
            return result;
        } catch (JSchException | IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
        }
    }

    public void upload(String location, File file) throws MojoExecutionException {
        ChannelSftp channel;
        if (!file.isFile()) {
            throw new MojoExecutionException("File " + file.getAbsolutePath() + " not a file");
        }
        if (!file.canRead()) {
            throw new MojoExecutionException("Cannot read file " + file.getAbsolutePath());
        }
        String destinationFile = location + "/" + file.getName();
        try {
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect(15000);
            String remoteDir = channel.pwd();
        } catch (JSchException | SftpException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
        try {
            channel.put(new FileInputStream(file), destinationFile, ChannelSftp.OVERWRITE);

        } catch (FileNotFoundException | SftpException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
        }
    }
}
