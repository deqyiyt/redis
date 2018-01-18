/**
 * 
 */

package com.ias.assembly.redis.bean;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@ToString
public class User implements Serializable {

    private static final long serialVersionUID = 196395979372889848L;

    private String name;

    private Date created;

    private int age;


    public User(String name, int age) {
        this.name = name;
        this.created = new Date();
        this.age = age;
    }
}
