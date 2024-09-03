package day3;

import java.io.Serializable;

public class Person2 implements Serializable {
    private String name;
    private transient int age; // 使用transient关键字修饰的字段不会被序列化

    public Person2(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person2 [name=" + name + ", age=" + age + "]";
    }
}
