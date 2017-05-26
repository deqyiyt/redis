package com.ias.assembly.redis.manager.impl;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ias.assembly.redis.command.CacheExpiredCommand;
import com.ias.assembly.redis.common.Constants.CacheModule;
import com.ias.assembly.redis.exception.CacheException;
import com.ias.assembly.redis.manager.CacheManager;
import com.ias.assembly.redis.prop.RedisProp;
import com.ias.assembly.redis.utils.MD5Util;
import com.ias.assembly.redis.utils.SerializableUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class CacheManagerImpl implements CacheManager {
	private final static Logger logger = LoggerFactory.getLogger(CacheManagerImpl.class);

	/**
	 * 默认情况下返回的filedName
	 */
	private static final String DEFAULT_FIELD = "default-field";

	@Autowired
    private RedisProp redisProp;

	@Autowired
	private JedisPool jedisPool;

	private Jedis getJedis() {
		return jedisPool.getResource();
	}

	/**
	 * 无论是key为null或是配置的key前辍为null,都返回key的值
	 * 
	 * @param key
	 * @return
	 */
	private String processKey(CacheModule module, String key) {

		String confKeyStart = null;

		if (redisProp != null)
			confKeyStart = redisProp.getKeyStart();

		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(confKeyStart))
			return key;
		if("*".equals(key)) {
			return confKeyStart + module.getName() + key;
		}
		key = MD5Util.getMD5Str(key);
		return confKeyStart + module.getName() + key;

	}

	/**
	 * 返回true表示会使用缓存
	 * 
	 * @return
	 */
	private boolean useCache() {

		boolean hasCache = true;

		if (redisProp != null)
			hasCache = redisProp.getHasCache();

		if (hasCache) {
			return true;
		}

		return false;
	}

	private void returnResource(Jedis redis) {
		if (redis != null) {
			redis.close();
		}
	}

	@Override
	public void setValue(CacheModule module, String key, String value, Integer expireSeconds) {
		if (!useCache())
			return;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			jredis.setex(key, expireSeconds, value);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public void setValue(CacheModule module, String key, String value) {
		if (!useCache())
			return;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			jredis.set(key, value);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}
	
	@Override
	public <T> void setObject(CacheModule module, String key, T t) {
		if (!useCache())
			return;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			String value = SerializableUtil.convert2String((Serializable) t);
			jredis = getJedis();
			jredis.set(key, value);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public <T> void setObject(CacheModule module, String key, T t, Integer expireSeconds) {
		if (!useCache())
			return;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			String value = SerializableUtil.convert2String((Serializable) t);
			jredis = getJedis();
			jredis.setex(key, expireSeconds, value);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getObject(CacheModule module, String key) {
		if (!useCache())
			return null;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			String value = jredis.get(key);
			if (StringUtils.isEmpty(value)) {
				return null;
			}
			return (T) SerializableUtil.convert2Object(value);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public String getValue(CacheModule module, String key) {
		if (!useCache())
			return null;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.get(key);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public Map<String, String> getAllMap(CacheModule module, String key) {
		if (!useCache())
			return null;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.hgetAll(key);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public void pushToListHead(CacheModule module, String key, String[] values) {
		if (!useCache())
			return;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			jredis.lpush(key, values);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public void pushToListHead(CacheModule module, String key, String value) {
		if (!useCache())
			return;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			jredis.lpush(key, value);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public void pushToListFooter(CacheModule module, String key, String[] values) {
		if (!useCache())
			return;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			jredis.rpush(key, values);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public void pushToListFooter(CacheModule module, String key, String value) {
		if (!useCache())
			return;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			jredis.rpush(key, value);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public String popListHead(CacheModule module, String key) {
		if (!useCache())
			return null;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.lpop(key);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public String popListFooter(CacheModule module, String key) {
		if (!useCache())
			return null;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.rpop(key);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public String findListItem(CacheModule module, String key, long index) {
		if (!useCache())
			return null;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.lindex(key, index);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public List<String> findLists(CacheModule module, String key, long start, long end) {
		if (!useCache())
			return null;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.lrange(key, start, end);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}

	}

	@Override
	public long listLen(CacheModule module, String key) {
		if (!useCache())
			return 0;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.llen(key);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public void addSet(CacheModule module, String key, String[] values) {
		if (!useCache())
			return;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			jredis.sadd(key, values);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public String popSet(CacheModule module, String key) {
		if (!useCache())
			return null;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.spop(key);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public boolean existsInSet(CacheModule module, String key, String member) {
		if (!useCache())
			return false;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.sismember(key, member);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public Set<String> findSetAll(CacheModule module, String key) {
		if (!useCache())
			return null;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.smembers(key);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public long findSetCount(CacheModule module, String key) {
		if (!useCache())
			return 0;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.scard(key);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}

	}

	@Override
	public void addSortSet(CacheModule module, String key, String value, long sortNo) {
		if (!useCache())
			return;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			jredis.zadd(key, sortNo, value);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}

	}

	@Override
	public Set<String> findSortSets(CacheModule module, String key, long start, long end) {
		if (!useCache())
			return null;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.zrange(key, start, end);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public long findSortSetCount(CacheModule module, String key) {
		if (!useCache())
			return 0;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.zcard(key);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public long findSortSetCount(CacheModule module, String key, long min, long max) {
		if (!useCache())
			return 0;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.zcount(key, min, max);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}
	
	@Override
	public Long remove(CacheModule module, String key) {
		if (!useCache())
			return null;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			return jredis.del(key);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}
	

	@Override
	public int removeForPattern(CacheModule module, String pattern) {
		if (!useCache())
			return 0;
		pattern = processKey(module, pattern);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			Set<String> set = jredis.keys(pattern);
			logger.info("++++redis keys pattern={}， 匹配的key set={}", pattern, set);
			if (set !=null && !set.isEmpty()) {
				String[] array = new String[set.size()];
				Iterator<String> it = set.iterator();
				int i = 0;
		        while(it.hasNext()){
		        	array[i] = it.next();
		        	i++;
		        }
		        jredis.del(array);
		        return set.size();
			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public void removeMapValue(CacheModule module, String key, String field) {
		if (!useCache())
			return;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			jredis.hdel(key, field);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public void setMapValue(CacheModule module, String key, String field, String value, int seconds) {
		if (!useCache())
			return;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();

			CacheExpiredCommand<String> cec = new CacheExpiredCommand<String>();

			cec.setObject(value);
			cec.setExpiredTime(System.currentTimeMillis() + seconds * 1000l);

			jredis.hset(key, field, SerializableUtil.convert2String((Serializable) cec));
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public String getMapValue(CacheModule module, String key, String field) {
		if (!useCache())
			return null;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			String value = jredis.hget(key, field);

			if (StringUtils.isEmpty(value)) {
				return null;
			}
			CacheExpiredCommand<String> cec = (CacheExpiredCommand<String>) SerializableUtil.convert2Object(value);

			if (System.currentTimeMillis() < cec.getExpiredTime())
				return cec.getObject();
			else
				return null;
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public <T> void setMapObject(CacheModule module, String key, String field, T t, int seconds) {
		if (!useCache())
			return;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();

			CacheExpiredCommand<T> cec = new CacheExpiredCommand<T>();

			cec.setObject(t);
			cec.setExpiredTime(System.currentTimeMillis() + seconds * 1000l);
			String value = SerializableUtil.convert2String((Serializable) cec);
			jredis.hset(key, field, value);
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getMapObject(CacheModule module, String key, String field) {
		if (!useCache())
			return null;
		key = processKey(module, key);
		Jedis jredis = null;
		try {
			jredis = getJedis();
			String value = jredis.hget(key, field);
			if (StringUtils.isEmpty(value)) {
				return null;
			}
			CacheExpiredCommand<T> cec = (CacheExpiredCommand<T>) SerializableUtil.convert2Object(value);
			if (System.currentTimeMillis() < cec.getExpiredTime())
				return (T) cec.getObject();
			else
				return null;
		} catch (Exception e) {
			throw new CacheException("51001", e.getMessage());
		} finally {
			returnResource(jredis);
		}
	}

	@Override
	public String generateMapFieldByDefault(Object... objArray) {
		StringBuffer sb = new StringBuffer();

		for (Object obj : objArray) {
			sb.append(obj.toString() + "-");
		}
		if (sb.length() > 0) {
			sb = sb.delete(sb.length() - 1, sb.length());
		} else {
			sb.append(DEFAULT_FIELD);

		}
		return sb.toString();
	}

	@Override
	public void expire(CacheModule module, String key, int timeout) {
		if (useCache()) {
			key = processKey(module, key);
			Jedis jredis = null;
			try {
				jredis = getJedis();
				jredis.expire(key, timeout);
			} catch (Exception e) {
				throw new CacheException("51001", e.getMessage());
			} finally {
				returnResource(jredis);
			}
		}
	}
}
