/**
 * 
 */

package com.ias.assembly.redis.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * redis配置信息Bean
 *
 * @author: jiuzhou.hu
 * @create: 2016-05-16 11:14
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProp {
	
    /**
     * 获取key的前辍，前辍用于解决不同的项目使用同一个redis环境的重名问题
     */
    private String keyStart;
    
    /**
     * 缓存开关
     */
    private boolean hasCache;
}
