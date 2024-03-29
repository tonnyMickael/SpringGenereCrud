package com.dev.template_crud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Configuration
public class AppConfiguration   {
	
	@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer() {
		FreeMarkerConfigurer freeMarkConfig = new FreeMarkerConfigurer();
		freeMarkConfig.setTemplateLoaderPath("classpath:/templates");
		freeMarkConfig.setDefaultEncoding("UTF-8");
		return freeMarkConfig;
	}
	
}
