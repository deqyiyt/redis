package com.ias.assembly.redis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.ias.assembly.redis.bean.User;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserService {
	
	@Cacheable
	public List<User> all() {
		log.debug("我来取数据了！");
		List<User> list = new ArrayList<>();
		list.add(new User("zhangsan", 5));
		list.add(new User("李四", 15));
		return list;
	}
}
