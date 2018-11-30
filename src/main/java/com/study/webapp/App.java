package com.study.webapp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 开发环境下的启动服务器类
 * 
 * @author xwb
 * @since 2016年1月13日
 */
@SpringBootApplication
@EnableAutoConfiguration
@EnableTransactionManagement
@MapperScan("com.zhonghui.exceltranslate.webapp.mapper")
@ComponentScan(basePackages = { "com.zhonghui.exceltranslate.webapp", "com.zhonghui.core" })
@EnableAsync
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
