package com.ias.assembly.redis.manager;

import com.ias.assembly.redis.common.Constants.CacheModule;

public interface RedisManager {
	/**
	 * 清空Module相关所有查询缓存
	 * @Method clear方法.<br>
	 * @author jiuzhou.hu
	 * @date 2016年9月19日 下午8:24:59
	 */
	void clear(CacheModule cacheModule);
}
