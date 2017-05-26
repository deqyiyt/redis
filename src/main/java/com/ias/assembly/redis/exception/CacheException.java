/**
 * 
 */

package com.ias.assembly.redis.exception;

/**
 * 缓存类异常
 *
 * @author: chenbing
 * @create: 2016-07-29 16:35
 */
public class CacheException extends RuntimeException {

    private static final long serialVersionUID = -5311888251857095112L;

    private String code;

    public CacheException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
