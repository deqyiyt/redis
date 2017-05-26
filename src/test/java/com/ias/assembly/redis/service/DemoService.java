package com.ias.assembly.redis.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@CacheConfig(cacheNames={
	"com.ias.assembly.redis.service.UserService"
})
public class DemoService {
	
	@Cacheable
	public String sayHello(String txt) {
		log.debug("进入sayHello，参数:{}", txt);
		return "hello " + txt;
	}
}
