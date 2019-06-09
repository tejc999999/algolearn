package jp.spring.boot.algolearn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import jp.spring.boot.algolearn.config.AceProperties;
import jp.spring.boot.algolearn.config.AcePropertiesConfig;

@Configuration
@ComponentScan("jp.spring.boot.algolearn")
public class AppConfig {

	@Autowired
	private AcePropertiesConfig acePropertiesConfig;
	
	@Bean
	public AceProperties aceProperties() {
		return acePropertiesConfig.aceProperties();
	}

//	@Bean
//	public MessageSource messageSource() {
//		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//		messageSource.setBasenames("ace");
//		return messageSource;
//	}
}
