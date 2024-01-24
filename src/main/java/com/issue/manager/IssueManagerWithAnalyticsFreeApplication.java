package com.issue.manager;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Log4j2
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class IssueManagerWithAnalyticsFreeApplication {

	public static void main(String[] args) {
		SpringApplication.run(IssueManagerWithAnalyticsFreeApplication.class, args);
	}

}
