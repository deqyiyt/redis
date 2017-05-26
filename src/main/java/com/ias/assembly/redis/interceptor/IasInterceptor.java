package com.ias.assembly.redis.interceptor;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.cache.interceptor.CacheOperationInvoker;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;

import com.ias.assembly.redis.annotation.NoCacheable;

public class IasInterceptor extends CacheInterceptor {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 375434945214451784L;
	
	@Autowired
	private CacheManager cacheManager;
	@Autowired
	private CacheResolver cacheResolver;
	@Autowired
	private KeyGenerator keyGenerator;
	@Autowired
	private CacheErrorHandler errorHandler;
	
	@Override
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();

		CacheOperationInvoker aopAllianceInvoker = new CacheOperationInvoker() {
			@Override
			public Object invoke() {
				try {
					return invocation.proceed();
				}
				catch (Throwable ex) {
					throw new ThrowableWrapper(ex);
				}
			}
		};

		try {
			if(method.getAnnotation(NoCacheable.class) != null || invocation.getThis().getClass().getAnnotation(NoCacheable.class) != null) {
				return aopAllianceInvoker.invoke();
			} else {
				return execute(aopAllianceInvoker, invocation.getThis(), method, invocation.getArguments());
			}
		}
		catch (CacheOperationInvoker.ThrowableWrapper th) {
			throw th.getOriginal();
		}
	}

	/**
	 * cacheManager的获取.
	 * @return CacheManager
	 */
	public CacheManager getCacheManager() {
		return cacheManager;
	}

	/**
	 * cacheResolver的获取.
	 * @return CacheResolver
	 */
	public CacheResolver getCacheResolver() {
		return cacheResolver;
	}

	/**
	 * keyGenerator的获取.
	 * @return KeyGenerator
	 */
	public KeyGenerator getKeyGenerator() {
		return keyGenerator;
	}

	/**
	 * errorHandler的获取.
	 * @return CacheErrorHandler
	 */
	public CacheErrorHandler getErrorHandler() {
		return errorHandler;
	}
	
}
