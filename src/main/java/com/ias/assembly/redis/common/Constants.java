package com.ias.assembly.redis.common;

public class Constants {

	public static final String API_ACCESS_KEY_PREFIX = "access/";
    
    public static final String API_AUTH_HASH_KEY_UID = "uid";
    
    public static final String API_AUTH_HASH_KEY_PHONE = "phone";
    
    public static final String API_AUTH_HASH_KEY_SECURITY = "security";
    
    public static final String API_AUTH_HASH_KEY_REFRESH_TOKEN = "rt";
    
    public static final String API_AUTH_REFRESH_TOKEN_KEY_PREFIX = "auth/rt/";
    
    public static final String API_AUTH_HASH_KEY_ACCESS = "auth";
    
    public static final String API_REGIST_HASH_KEY_ACCESS = "regist";
    
    /**
     * 缓存模块名
     */
    public enum CacheModule {
    	WULIU("wuliu/"), OWNER("owner/"), DRIVER("driver/"), SHOP("shop/"), SMS("sms/"), DAO("dao/"), SERVICE("service/");

        private String name;
        
        CacheModule(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class CacheKey {
        public static final String KEY = "ias/cache/redis/";
        
        public static final String SERVICE = KEY + CacheModule.SERVICE.getName();
        
        public static final String DAO = KEY + CacheModule.DAO.getName();
    }
    
    public static class AnnotationKey {
        public static final String SERVICE_ = "'ias/cache/redis/service/'+";
    }
    
    public static class Cache {
    	/**
    	 * service的缓存过期时间
    	 */
        public static final long DEFAULT_EXPIRATION = 24 * 60 * 60;
        
        /**
         * 用户过期时间
         */
        public static final int API_REDIS_TIMEOUT = 168 * 60 * 60;
    }
}
