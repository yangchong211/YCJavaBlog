package day3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Person implements Serializable {

    private String name;
    private int age;
    private transient String address; // The field marked as transient will not be serialized

    public Person(String name, int age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        // Manually control the serialization process
        out.defaultWriteObject(); // Default serialization of name and age fields
        // Custom serialization for the address field
        out.writeUTF(address);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        // Manually control the deserialization process
        in.defaultReadObject(); // Default deserialization of name and age fields
        // Custom deserialization for the address field
        address = in.readUTF();
    }

    @Override
    public String toString() {
        return "Person [name=" + name + ", age=" + age + ", address=" + address + "]";
    }

}
