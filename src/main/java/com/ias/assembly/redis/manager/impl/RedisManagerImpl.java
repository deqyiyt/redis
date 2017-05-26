package com.ias.assembly.redis.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ias.assembly.redis.manager.CacheManager;
import com.ias.assembly.redis.common.Constants.CacheModule;
import com.ias.assembly.redis.manager.RedisManager;

@Service
public class RedisManagerImpl implements RedisManager {

	@Autowired
	CacheManager cacheManager;
	
	@Override
	public void clear(CacheModule cacheModule) {
		cacheManager.removeForPattern(cacheModule, "*");
	}
}
