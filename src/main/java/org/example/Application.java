package org.example;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.config.RuntimeConfigManager;
import org.springframework.config.RuntimeConfigBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;

@Configuration
@ComponentScan
@EnableMBeanExport
@EnableAutoConfiguration
public class Application {

	public static void main(String[] args) throws Exception {

		ApplicationContext ctx = SpringApplication.run(Application.class, args);

		BusinessComponent bean = ctx.getBean(BusinessComponent.class);

		while (true) {
			bean.businessMethod();
			TimeUnit.SECONDS.sleep(1);
		}
	}

	@Bean
	public BusinessComponent componentA() {
		return new BusinessComponent();
	}

	@Bean
	public RuntimeConfigBeanPostProcessor configSettingBeanPostProcessor() {
		return new RuntimeConfigBeanPostProcessor();
	}

	@Bean
	public RuntimeConfigManager configManager() {
		return new RuntimeConfigManager();
	}
}
