/**
 * 
 */

package com.ias.assembly.redis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * redis单元测试类
 *
 * @author: chenbing
 * @create: 2016-05-16 14:28
 */
@SuppressWarnings("ALL")
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = {RedisTestApplication.class})
public class RedisTest {

    //    @Autowired
    private RedisTemplate redisTemplate;

    //    @Test
    public void test1() {
        ValueOperations<String, String> valueOper = redisTemplate.opsForValue();
//        valueOper.set("ux:u1", "this is a test! 阿门");
        valueOper.increment("dx:num1", 1L);
//        User user1 = new User("zhangsan", 5);
//        User user2 = new User("李四", 15);
//        valueOper.set("u:u1", user1);
//        valueOper.set("u:u2", user2);
        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return new String(redisConnection.get("dx:num1".getBytes()));
            }
        });
        System.out.println(obj.toString());
//        System.out.println(valueOper.get("u:u2"));
    }
}
