/**
 * 
 */

package com.ias.assembly.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import com.ias.assembly.redis.spring.RedisAnnotationBeanNameGenerator;

@SpringBootConfiguration
@ComponentScan(basePackages = {"com.ias.**.config"}, nameGenerator = RedisAnnotationBeanNameGenerator.class)
public class TestApplication extends SpringBootServletInitializer{

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TestApplication.class, args);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TestApplication.class);
    }
}
