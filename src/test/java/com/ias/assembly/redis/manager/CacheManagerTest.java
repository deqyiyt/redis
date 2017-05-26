package com.ias.assembly.redis.manager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ias.assembly.redis.common.Constants.CacheModule;
import com.ias.assembly.redis.manager.CacheManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheManagerTest {
	
	@Autowired
	private CacheManager cacheManager;
	
	@Test
	public void clear() {
		cacheManager.removeForPattern(CacheModule.SERVICE, "*");
		cacheManager.removeForPattern(CacheModule.DAO, "*");
	}
}
