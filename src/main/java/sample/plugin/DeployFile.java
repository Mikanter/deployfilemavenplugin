package sample.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

/**
 * Uploads a file and runs a ssh command
 */
@Mojo(name = "deployFile")

public class DeployFile extends AbstractMojo {
    @Parameter(property = "exec.remote.ssh.host")
    private String sshHost;
    @Parameter(property = "exec.remote.ssh.user")
    private String sshUser;
    @Parameter(property = "exec.remote.ssh.password")
    private String sshPassword;
    @Parameter(property = "exec.remote.ssh.command")
    private String sshCommand;
    @Parameter(property = "exec.upload.file")
    private String uploadFile;
    @Parameter(property = "exec.remote.upload.destination")
    private String uploadDestination;

    public void execute() throws MojoExecutionException {
        SshRun ssh = new SshRun();
        ssh.connect(sshHost, sshUser, sshPassword);
        File file = new File(uploadFile);

        ssh.upload(uploadDestination, file);
        getLog().info("Uploaded file to remote machine " + sshHost);

        String result = ssh.exec(sshCommand);
        getLog().info("Run the command: " + sshCommand);
        getLog().info("Result is: " + result);
    }
}