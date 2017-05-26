package com.ias.assembly.config;

import org.springframework.cache.annotation.AnnotationCacheOperationSource;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ias.assembly.redis.interceptor.IasInterceptor;

/** 
 * 代理拦截器
 * @author: jiuzhou.hu
 * @date:2017年5月18日下午7:18:03 
 */
@Configuration
public class ProxyCachingConfig {

	@Bean
	public CacheInterceptor cacheInterceptor(AnnotationCacheOperationSource cacheOperationSource) {
		IasInterceptor interceptor = new IasInterceptor();
		interceptor.setCacheOperationSources(cacheOperationSource);
		return interceptor;
	}
}
