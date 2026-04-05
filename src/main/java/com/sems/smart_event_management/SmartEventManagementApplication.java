package com.sems.smart_event_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync

@EntityScan(basePackages = "com.sems")
// This tells Spring Boot: "scan the ENTIRE com.sems package for @Entity classes"
// Without this, Spring only scans com.sems.smart_event_management
// and misses User.java and Event.java which are in com.sems.entity
// This is the root cause of why your tables weren't created

@EnableJpaRepositories(basePackages = "com.sems")
// This tells Spring: "scan entire com.sems for Repository interfaces"
// Needed because repositories are in com.sems.repository
// which is also outside the main class package
public class SmartEventManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartEventManagementApplication.class, args);
	}
}