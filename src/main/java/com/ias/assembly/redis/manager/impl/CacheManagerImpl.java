package com.ias.assembly.redis.manager.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ias.assembly.redis.command.CacheExpiredCommand;
import com.ias.assembly.redis.common.Constants.CacheModule;
import com.ias.assembly.redis.manager.CacheManager;
import com.ias.assembly.redis.prop.RedisProp;
import com.ias.assembly.redis.utils.MD5Util;
import com.ias.assembly.redis.utils.SerializableUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CacheManagerImpl implements CacheManager {

    /**
     * 默认情况下返回的filedName
     */
    private static final String DEFAULT_FIELD = "default-field";
    
    @Autowired
    private RedisProp redisProp;
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


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

    @Override
    public void setValue(CacheModule module, String key, String value, Integer expireSeconds) {
        if (!useCache())
            return;
        key = processKey(module, key);
        stringRedisTemplate.opsForValue().set(key, value, expireSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void setValue(CacheModule module, String key, String value) {
        if (!useCache())
            return;
        key = processKey(module, key);
        stringRedisTemplate.opsForValue().set(key, value);
    }
    
    @Override
    public <T> void setObject(CacheModule module, String key, T t) {
        if (!useCache())
            return;
        String value = SerializableUtil.convert2String((Serializable) t);
        this.setValue(module, key, value);
    }

    @Override
    public <T> void setObject(CacheModule module, String key, T t, Integer expireSeconds) {
        if (!useCache())
            return;
        String value = SerializableUtil.convert2String((Serializable) t);
        this.setValue(module, key, value, expireSeconds);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getObject(CacheModule module, String key) {
        if (!useCache())
            return null;
        String value = this.getValue(module, key);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return (T) SerializableUtil.convert2Object(value);
    }

    @Override
    public String getValue(CacheModule module, String key) {
        if (!useCache())
            return null;
        key = processKey(module, key);
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public Map<Object, Object> getAllMap(CacheModule module, String key) {
        if (!useCache())
            return null;
        key = processKey(module, key);
        return stringRedisTemplate.opsForHash().entries(key);
    }

    @Override
    public void pushToListHead(CacheModule module, String key, String[] values) {
        if (!useCache())
            return;
        key = processKey(module, key);
        stringRedisTemplate.boundListOps(key).leftPushAll(values);
    }

    @Override
    public void pushToListHead(CacheModule module, String key, String value) {
        if (!useCache())
            return;
        key = processKey(module, key);
        stringRedisTemplate.boundListOps(key).leftPush(value);
    }

    @Override
    public void pushToListFooter(CacheModule module, String key, String[] values) {
        if (!useCache())
            return;
        key = processKey(module, key);
        stringRedisTemplate.boundListOps(key).rightPushAll(values);
    }

    @Override
    public void pushToListFooter(CacheModule module, String key, String value) {
        if (!useCache())
            return;
        key = processKey(module, key);
        stringRedisTemplate.boundListOps(key).rightPush(value);
    }

    @Override
    public String popListHead(CacheModule module, String key) {
        if (!useCache())
            return null;
        key = processKey(module, key);
        return stringRedisTemplate.boundListOps(key).leftPop();
    }

    @Override
    public String popListFooter(CacheModule module, String key) {
        if (!useCache())
            return null;
        key = processKey(module, key);
        return stringRedisTemplate.boundListOps(key).rightPop();
    }

    @Override
    public String findListItem(CacheModule module, String key, long index) {
        if (!useCache())
            return null;
        key = processKey(module, key);
        return stringRedisTemplate.boundListOps(key).index(index);
    }

    @Override
    public List<String> findLists(CacheModule module, String key, long start, long end) {
        if (!useCache())
            return null;
        key = processKey(module, key);
        return stringRedisTemplate.boundListOps(key).range(start, end);
    }

    @Override
    public long listLen(CacheModule module, String key) {
        if (!useCache())
            return 0;
        key = processKey(module, key);
        return stringRedisTemplate.boundListOps(key).size();
    }

    @Override
    public void addSet(CacheModule module, String key, String[] values) {
        if (!useCache())
            return;
        key = processKey(module, key);
        stringRedisTemplate.boundSetOps(key).add(values);
    }

    @Override
    public String popSet(CacheModule module, String key) {
        if (!useCache())
            return null;
        key = processKey(module, key);
        return stringRedisTemplate.boundSetOps(key).pop();
    }

    @Override
    public boolean existsInSet(CacheModule module, String key, String member) {
        if (!useCache())
            return false;
        key = processKey(module, key);
        return stringRedisTemplate.boundSetOps(key).isMember(member);
    }

    @Override
    public Set<String> findSetAll(CacheModule module, String key) {
        if (!useCache())
            return null;
        key = processKey(module, key);
        return stringRedisTemplate.boundSetOps(key).members();
    }

    @Override
    public long findSetCount(CacheModule module, String key) {
        if (!useCache())
            return 0;
        key = processKey(module, key);
        return stringRedisTemplate.boundSetOps(key).size();
    }

    @Override
    public void addSortSet(CacheModule module, String key, String value, long sortNo) {
        if (!useCache())
            return;
        key = processKey(module, key);
        stringRedisTemplate.boundZSetOps(key).add(value, sortNo);
    }

    @Override
    public Set<String> findSortSets(CacheModule module, String key, long start, long end) {
        if (!useCache())
            return null;
        key = processKey(module, key);
        return stringRedisTemplate.boundZSetOps(key).range(start, end);
    }

    @Override
    public long findSortSetCount(CacheModule module, String key) {
        if (!useCache())
            return 0;
        key = processKey(module, key);
        return stringRedisTemplate.boundZSetOps(key).size();
    }

    @Override
    public long findSortSetCount(CacheModule module, String key, long min, long max) {
        if (!useCache())
            return 0;
        key = processKey(module, key);
        return stringRedisTemplate.boundZSetOps(key).count(min, max);
    }
    
    @Override
    public void remove(CacheModule module, String key) {
        if (!useCache())
            return;
        key = processKey(module, key);
        stringRedisTemplate.delete(key);
    }
    

    @Override
    public int removeForPattern(CacheModule module, String pattern) {
        if (!useCache())
            return 0;
        pattern = processKey(module, pattern);
        Set<String> set = stringRedisTemplate.keys(pattern);
        log.info("++++redis keys pattern={}， 匹配的key set={}", pattern, set);
        if (set !=null && !set.isEmpty()) {
            stringRedisTemplate.delete(set);
            return set.size();
        } else {
            return 0;
        }
    }

    @Override
    public void removeMapValue(CacheModule module, String key, String field) {
        if (!useCache())
            return;
        key = processKey(module, key);
        stringRedisTemplate.boundHashOps(key).delete(field);
    }

    @Override
    public void setMapValue(CacheModule module, String key, String field, String value, int seconds) {
        if (!useCache())
            return;
        key = processKey(module, key);

        CacheExpiredCommand<String> cec = new CacheExpiredCommand<String>();

        cec.setObject(value);
        cec.setExpiredTime(System.currentTimeMillis() + seconds * 1000l);
        
        stringRedisTemplate.boundHashOps(key).put(field, SerializableUtil.convert2String((Serializable) cec));
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getMapValue(CacheModule module, String key, String field) {
        if (!useCache())
            return null;
        key = processKey(module, key);
        Object value = stringRedisTemplate.boundHashOps(key).get(field);

        if (StringUtils.isEmpty(value)) {
            return null;
        }
        CacheExpiredCommand<String> cec = (CacheExpiredCommand<String>) SerializableUtil.convert2Object((String)value);

        if (System.currentTimeMillis() < cec.getExpiredTime())
            return cec.getObject();
        else
            return null;
    }

    @Override
    public <T> void setMapObject(CacheModule module, String key, String field, T t, int seconds) {
        if (!useCache())
            return;
        key = processKey(module, key);

        CacheExpiredCommand<T> cec = new CacheExpiredCommand<T>();

        cec.setObject(t);
        cec.setExpiredTime(System.currentTimeMillis() + seconds * 1000l);
        String value = SerializableUtil.convert2String((Serializable) cec);
        stringRedisTemplate.boundHashOps(key).put(key, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getMapObject(CacheModule module, String key, String field) {
        if (!useCache())
            return null;
        key = processKey(module, key);
        Object value = stringRedisTemplate.boundHashOps(key).get(field);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        CacheExpiredCommand<T> cec = (CacheExpiredCommand<T>) SerializableUtil.convert2Object((String)value);
        if (System.currentTimeMillis() < cec.getExpiredTime())
            return (T) cec.getObject();
        else
            return null;
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
            stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        }
    }
}
