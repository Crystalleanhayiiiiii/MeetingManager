package com.api.meetingservice.meetingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient 
public class MeetingserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeetingserviceApplication.class, args);
	}
}
