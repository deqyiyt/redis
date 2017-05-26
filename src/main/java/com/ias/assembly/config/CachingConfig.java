/**
 * 
 */

package com.ias.assembly.config;

import static com.ias.assembly.redis.common.Constants.Cache.DEFAULT_EXPIRATION;
import static com.ias.assembly.redis.common.Constants.CacheKey.SERVICE;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import com.ias.assembly.redis.utils.JsonUtil;
import com.ias.assembly.redis.utils.MD5Util;
import com.ias.assembly.redis.utils.SerializableUtil;

import lombok.extern.slf4j.Slf4j;

/** 
 * 环境策略
 * @author: jiuzhou.hu
 * @date:2017年5月18日下午7:17:54 
 */
@Configuration
@Slf4j
public class CachingConfig implements CachingConfigurer {

	@Resource(name = "redisTemplate")
    private RedisTemplate<?,?> redisTemplate;
	
	@Override
	@Bean
	public CacheManager cacheManager() {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		cacheManager.setDefaultExpiration(DEFAULT_EXPIRATION);
		return cacheManager;
	}

	@Override
	@Bean
	public CacheResolver cacheResolver() {
		SimpleCacheResolver cacheResolver = new SimpleCacheResolver(cacheManager()){
			@Override
			protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
				Set<String> cacheNames = context.getOperation().getCacheNames();
				Object object = context.getTarget();
				CacheConfig cacheConfig = object.getClass().getAnnotation(CacheConfig.class);
				if(cacheConfig != null && cacheConfig.cacheNames() != null && cacheConfig.cacheNames().length > 0) {
					for(String str:cacheConfig.cacheNames()) {
						cacheNames.add(str);
					}
				}
				cacheNames.add(object.getClass().getName());
				return packageCacheNames(cacheNames);
			}
			
			private List<String> packageCacheNames(Set<String> cacheNames) {
				List<String> list = new ArrayList<String>();
				for (String str : cacheNames) {
					if(str.startsWith(SERVICE)) {
						list.add(str);
					} else {
						list.add(SERVICE + MD5Util.md5(SerializableUtil.convert2String(str).getBytes()));
					}
				}
				return list;
			}
		};
		return cacheResolver;
	}

	@Override
	@Bean
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
        		StringBuffer key = new StringBuffer()
        				.append(target.getClass().getCanonicalName())
        				.append(method.getName());
        		if (params.length > 0) {
        			for(Object object:params) {
        				key.append(JsonUtil.buildNormalBinder().toJson(object));
        			}
        		}
        		return SERVICE + MD5Util.md5(SerializableUtil.convert2String(key).getBytes());
            }
        };
	}

	@Override
	@Bean
	public CacheErrorHandler errorHandler() {
		return new CacheErrorHandler() {
			@Override
			public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
				log.debug("cache get error");
			}

			@Override
			public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
				log.debug("cache put error");
			}

			@Override
			public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
				log.debug("cache evict error");
			}

			@Override
			public void handleCacheClearError(RuntimeException exception, Cache cache) {
				log.debug("cache clear error");
			}
		};
	}
}
