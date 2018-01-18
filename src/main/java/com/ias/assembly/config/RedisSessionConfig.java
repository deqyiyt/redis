/**
 * 
 */

package com.ias.assembly.config;

import org.springframework.boot.autoconfigure.web.ServerProperties;
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
public class RedisSessionConfig {
	@Bean
	public static ConfigureRedisAction configureRedisAction() {
	    return ConfigureRedisAction.NO_OP;
	}
	
	/** 
	 * 修改cookie默认SSESIONID
	 * @author: jiuzhou.hu
	 * @date:2017年5月27日上午9:12:49 
	 * @return
	 */
	@Bean
    public CookieSerializer defaultCookieSerializer(ServerProperties serverProperties){
        DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
        defaultCookieSerializer.setCookieName(serverProperties.getSession().getCookie().getName());
        defaultCookieSerializer.setCookieMaxAge(serverProperties.getSession().getCookie().getMaxAge());
        defaultCookieSerializer.setCookiePath(serverProperties.getSession().getCookie().getPath());
        return defaultCookieSerializer;
    }
}
