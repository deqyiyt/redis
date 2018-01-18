package com.ias.assembly.redis.common;

public class Constants {
    
    /**
     * 缓存模块名
     */
    public enum CacheModule {
    	SMS("sms:"), DAO("dao:"), SERVICE("service:");

        private String name;
        
        CacheModule(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class CacheKey {
        public static final String KEY = "ias:cache:redis:";
        
        public static final String SERVICE = KEY + CacheModule.SERVICE.getName();
        
        public static final String DAO = KEY + CacheModule.DAO.getName();
    }
    
    public static class Cache {
    	/**
    	 * service的缓存过期时间
    	 */
        public static final long DEFAULT_EXPIRATION = 24 * 60 * 60;
    }
}
