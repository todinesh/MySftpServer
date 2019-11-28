package com.dishatech.mysftpserver.utils;

import com.dishatech.mysftpserver.service.SftpServerService;

import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.UserAuth;
import org.apache.sshd.server.auth.password.UserAuthPasswordFactory;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.shell.ProcessShellCommandFactory;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SftpServerUtils {

    public static SftpServerService setupSftpServer(String username, String password,String host, int port) throws IOException {
    	
    	String sftpHome ="src/test/resources";
    	
    	Path rootSftpDir = Paths.get(sftpHome).toAbsolutePath();
    	
        List<NamedFactory<UserAuth>> userAuthFactories = new ArrayList<>();
        userAuthFactories.add(new UserAuthPasswordFactory());

        List<NamedFactory<Command>> sftpCommandFactory = new ArrayList<>();
        sftpCommandFactory.add(new SftpSubsystemFactory());

        SshServer sshd = SshServer.setUpDefaultServer();
        sshd.setHost(host);
        sshd.setPort(port);
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
        sshd.setUserAuthFactories(userAuthFactories);
        sshd.setCommandFactory(new ProcessShellCommandFactory());
        sshd.setSubsystemFactories(sftpCommandFactory);
        sshd.setPasswordAuthenticator((usernameAuth, passwordAuth, session) -> {
            if ((username.equals(usernameAuth)) && (password.equals(passwordAuth))) {
                sshd.setFileSystemFactory(new VirtualFileSystemFactory(rootSftpDir));
                return true;
            }
            return false;
        });

        sshd.start();
        System.out.println("Started SFTP server on port " + port + " with root path: " + rootSftpDir.toFile().getAbsolutePath());
        return new SftpServerService(sshd, rootSftpDir);
    }

    public static void stopServer(SftpServerService sftpServerBean) throws IOException {
        sftpServerBean.getSshServer().stop();
        Files.walk(sftpServerBean.getRootPath())
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }
}
