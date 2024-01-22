package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestIssueManagerWithAnalyticsFreeApplication {

	public static void main(String[] args) {
		SpringApplication.from(IssueManagerWithAnalyticsFreeApplication::main).with(TestIssueManagerWithAnalyticsFreeApplication.class).run(args);
	}

}
