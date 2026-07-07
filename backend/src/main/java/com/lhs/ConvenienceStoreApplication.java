package com.lhs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.lhs")
@MapperScan("com.lhs.mapper")
public class ConvenienceStoreApplication {
	public static void main(String[] args) {
		SpringApplication.run(ConvenienceStoreApplication.class, args);
		System.out.println("便利店系统运行成功!");
	}
}