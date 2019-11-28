package com.dishatech.mysftpserver;

import com.dishatech.mysftpserver.service.SftpServerService;
import com.dishatech.mysftpserver.utils.SftpServerUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import javax.annotation.PostConstruct;

@SpringBootApplication
public class SftpServerApplication {

	@Value("${sftp.username}")
	private String username;
	
	@Value("${sftp.password}")
	private String password;
	
	@Value("${sftp.host}")
	private String host;
	
	@Value("${sftp.port}")
	int port;

    public static void main(String[] args) throws IOException {
    	SpringApplication.run(SftpServerApplication.class, args);
    }
    	
    @PostConstruct
	public void startSftpServer() {
        try {
			SftpServerService serverBean = SftpServerUtils.setupSftpServer(username, password, host, port);
	        while (true);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
