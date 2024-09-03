package day3;

import java.io.*;

public class SerializationExample2 {

    public static void main(String[] args) {
        Person2 person = new Person2("打工充", 30);
        try {
            FileOutputStream fileOut = new FileOutputStream("person.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(person); // 序列化Person2对象
            out.close();
            fileOut.close();
            FileInputStream fileIn = new FileInputStream("person.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Person2 restoredPerson2 = (Person2) in.readObject(); // 反序列化为Person2对象
            in.close();
            fileIn.close();
            System.out.println("Original Person2: " + person);
            System.out.println("Restored Person2: " + restoredPerson2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
