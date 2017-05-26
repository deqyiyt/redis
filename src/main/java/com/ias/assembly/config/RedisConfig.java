/**
 * 
 */

package com.ias.assembly.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * redis组件配置类
 *
 * @author: jiuzhou.hu
 * @create: 2016-05-16 11:15
 */
@Configuration
public class RedisConfig {

	@Value("${ias.redis.cookieName:IAS_ID}")
	private String cookieName;

	@Value("${ias.redis.expiration:-1}")
	private int expiration;

	@Bean
	public static ConfigureRedisAction configureRedisAction() {
	    return ConfigureRedisAction.NO_OP;
	}
	
	@Bean
    public CookieSerializer defaultCookieSerializer(){
        DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
        defaultCookieSerializer.setCookieName(cookieName);
        defaultCookieSerializer.setCookieMaxAge(expiration);
        return defaultCookieSerializer;
    }
}
