package day3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Person3 implements Serializable {

    private String name;
    private int age;
    private transient String address; // The field marked as transient will not be serialized
    private transient long cardId;

    public Person3(String name, int age, String address , long cardId) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.cardId = cardId;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        // Manually control the serialization process
        out.defaultWriteObject(); // Default serialization of name and age fields
        // Custom serialization for the address field
        out.writeUTF(address);
        out.writeLong(cardId);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        // Manually control the deserialization process
        in.defaultReadObject(); // Default deserialization of name and age fields
        // Custom deserialization for the address field
        address = in.readUTF();
        cardId = in.readLong();
    }

    @Override
    public String toString() {
        return "Person [name=" + name + ", age=" + age + ", address=" + address + ", cardId=" + cardId + "]";
    }

}
