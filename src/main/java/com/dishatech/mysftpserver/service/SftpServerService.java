package com.dishatech.mysftpserver.service;

import org.apache.sshd.server.SshServer;

import java.nio.file.Path;

public class SftpServerService {
    private SshServer sshServer;
    private Path rootPath;

    public SftpServerService(SshServer sshServer, Path rootPath) {
        this.sshServer = sshServer;
        this.rootPath = rootPath;
    }

    public SshServer getSshServer() {
        return sshServer;
    }

    public Path getRootPath() {
        return rootPath;
    }
}
