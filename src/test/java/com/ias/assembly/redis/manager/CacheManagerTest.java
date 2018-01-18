package com.ias.assembly.redis.manager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import com.ias.assembly.redis.common.Constants.CacheModule;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheManagerTest {
	
	@Autowired
	private CacheManager cacheManager;
	
	/**
	 * 测试清空缓存
	 * @author jiuzhou.hu
	 * @date 2018年1月18日 下午2:49:01
	 */
	@Test
	public void clear() {
		cacheManager.removeForPattern(CacheModule.SERVICE, "*");
		cacheManager.removeForPattern(CacheModule.DAO, "*");
	}
	
	/**
	 * 测试插入缓存
	 * @author jiuzhou.hu
	 * @date 2018年1月18日 下午2:49:11
	 */
	@Test
	public void put() {
		cacheManager.setValue(CacheModule.SERVICE, "test", "123");
	}
	
	
	/**
	 * 取出队列中的一条数据
	 * @author jiuzhou.hu
	 * @date 2018年1月18日 下午2:49:20
	 */
	@Test
    public void popListHead() {
        String userId = "12345678";
        String value = cacheManager.popListHead(CacheModule.SERVICE, userId);
        if(!StringUtils.isEmpty(value)) {
            System.out.println(value);
        }
    }
	
	/**
	 * 取出队列中所有数据
	 * @author jiuzhou.hu
	 * @date 2018年1月18日 下午2:49:38
	 */
	@Test
    public void popListHeadAll() {
	    String userId = "12345678";
        long count = cacheManager.listLen(CacheModule.SERVICE, userId);
        for(int i=0;i<count;i++) {
            String value = cacheManager.popListHead(CacheModule.SERVICE, userId);
            if(!StringUtils.isEmpty(value)) {
                System.out.println(value);
            }
        }
    }
	
	/**
	 * 往队列中插入一条记录
	 * @author jiuzhou.hu
	 * @date 2018年1月18日 下午2:49:48
	 */
	@Test
    public void pushToListHead() {
	    String userId = "12345678";
	    for(int i=0;i<10;i++) {
	        cacheManager.pushToListHead(CacheModule.SERVICE, userId, "value:"+i);
	    }
    }
}
