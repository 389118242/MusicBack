package com.back.music;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.back.music.conf.BackDataSourceConfig;
import com.back.music.interceptor.LoginInterceptor;

@SpringBootApplication
@EnableConfigurationProperties(value = BackDataSourceConfig.class)
public class RunApplication extends WebMvcConfigurerAdapter {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(RunApplication.class);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns("/login", "/error");
		super.addInterceptors(registry);
	}
}
