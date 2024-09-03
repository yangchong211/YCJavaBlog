package day3;

import java.io.*;

public class SerializationExample {

    public static void main(String[] args) {
        Person person = new Person("打工充", 30, "湖北武汉");
        try {
            //写数据
            FileOutputStream fileOut = new FileOutputStream("person.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(person); // Manually call writeObject to serialize
            out.close();
            fileOut.close();

            //读数据
            FileInputStream fileIn = new FileInputStream("person.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Person restoredPerson = (Person) in.readObject(); // Manually call readObject to deserialize
            in.close();
            fileIn.close();

            System.out.println("Original Person: " + person);
            System.out.println("Restored Person: " + restoredPerson);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
