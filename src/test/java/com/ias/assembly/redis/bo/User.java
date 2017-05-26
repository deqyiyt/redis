/**
 * 
 */

package com.ias.assembly.redis.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * 测试用User类
 *
 * @author: chenbing
 * @create: 2016-05-16 14:34
 */
public class User implements Serializable {

    private static final long serialVersionUID = 196395979372889848L;

    private String name;

    private Date created;

    private int age;

    public User() {
    }

    public User(String name, int age) {
        this.name = name;
        this.created = new Date();
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", created=" + created +
                ", age=" + age +
                '}';
    }
}
