package com.customer.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.Serializable;

@Document
public class Customer implements Serializable {

    @Id
    private String id;

    private Integer age;

    private String name;

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", age=" + age +
                ", name='" + name + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


