package com.photo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 定时器启动类
public class MyPhotoSvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyPhotoSvcApplication.class, args);
	}

}
