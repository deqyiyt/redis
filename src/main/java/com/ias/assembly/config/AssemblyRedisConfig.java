/**
 * 
 */

package com.ias.assembly.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.util.StringUtils;

import com.ias.assembly.redis.prop.RedisProp;
import com.ias.assembly.redis.spring.RedisAnnotationBeanNameGenerator;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPool;

/** 
 * redis组件配置类
 * @author: jiuzhou.hu
 * @date:2017年5月18日下午7:17:36 
 */
@Configuration
@EnableCaching
@EnableRedisHttpSession
@PropertySources({
        @PropertySource("classpath:config/ias-assembly-redis.properties"),
        @PropertySource(value = "file:/ias/config/ias-assembly-redis.properties", ignoreResourceNotFound = true)
})
@EnableConfigurationProperties({RedisProp.class})
@ComponentScan(basePackages = {"com.ias.assembly.redis"}, nameGenerator = RedisAnnotationBeanNameGenerator.class)
@Slf4j
public class AssemblyRedisConfig {

    @Autowired
    private RedisProp redisProp;

    @Bean
    public RedisConnectionFactory jedisConnectionFactory() {
        log.info("++++加载的redis信息 {}", redisProp.getHost());
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisProp.getPool());
        jedisConnectionFactory.setHostName(redisProp.getHost());
        jedisConnectionFactory.setPassword(redisProp.getPassword());
        jedisConnectionFactory.setPort(redisProp.getPort());
        jedisConnectionFactory.setDatabase(redisProp.getDatabase());
        jedisConnectionFactory.setUsePool(true);
        return jedisConnectionFactory;
    }

	@Bean
    public RedisTemplate<?,?> redisTemplate() {
    	RedisTemplate<?,?> redisTemplate = new RedisTemplate<>();
    	redisTemplate.setConnectionFactory(jedisConnectionFactory());
    	redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer()); redisTemplate
//        redisTemplate.setEnableTransactionSupport(false);
        return redisTemplate;
    }
    
    @Bean
    public JedisPool redisPoolFactory() {
        if(StringUtils.isEmpty(redisProp.getPassword())) {
            return new JedisPool(redisProp.getPool(), redisProp.getHost(), redisProp.getPort(), redisProp.getTimeout());
        } else {
            return new JedisPool(redisProp.getPool(), redisProp.getHost(), redisProp.getPort(), redisProp.getTimeout(), redisProp.getPassword());
        }
	}
}
