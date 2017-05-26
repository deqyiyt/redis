/**
 * 
 */

package com.ias.assembly.redis.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis配置信息Bean
 *
 * @author: jiuzhou.hu
 * @create: 2016-05-16 11:14
 */
@Data
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProp {
	
	/**
	 * 数据库索引（默认为0）
	 */
	private int database = 0;
	
	/**
	 * 服务器地址
	 */
	private String host;
	
	/**
	 * 服务器连接端口
	 */
	private int port;
	
	/**
	 * 服务器连接密码（默认为空）
	 */
	private String password;
	
	/**
	 * 连接超时时间（毫秒）
	 */
	private int timeout;
	
    /**
     * 获取key的前辍，前辍用于解决不同的项目使用同一个redis环境的重名问题
     */
    private String keyStart;
    
    /**
     * 缓存开关
     */
    private boolean hasCache;
    
    private JedisPoolConfig pool;
}
