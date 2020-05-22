package com.gf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages = { "com.**" })
public class SpringbootSecurityApplication extends SpringBootServletInitializer{

    public static void main(String[] args) {
        SpringApplication.run( SpringbootSecurityApplication.class, args );
    }

    @Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// TODO Auto-generated method stub
		return builder.sources(SpringbootSecurityApplication.class);
	}
    
    @Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
       CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
       return multipartResolver;
    }

}
