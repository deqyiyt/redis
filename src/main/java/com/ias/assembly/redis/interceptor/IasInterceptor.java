package com.ias.assembly.redis.interceptor;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.cache.interceptor.CacheOperationInvoker;

import com.ias.assembly.redis.annotation.NoCacheable;

public class IasInterceptor extends CacheInterceptor {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 375434945214451784L;
	
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
	
}
