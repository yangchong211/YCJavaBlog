package day3;

import java.io.*;

public class SerializationExample4 {

    public static void main(String[] args) {
//        Person4 person = new Person4("打工充", 30,"武汉", 2143324);
        try {
//            FileOutputStream fileOut = new FileOutputStream("person.txt");
//            ObjectOutputStream out = new ObjectOutputStream(fileOut);
//            out.writeObject(person);
//            out.close();
//            fileOut.close();

            FileInputStream fileIn = new FileInputStream("person.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Person4 restoredPerson2 = (Person4) in.readObject(); // 反序列化为Person2对象
            in.close();
            fileIn.close();
//            System.out.println("Original Person3: " + person);
            System.out.println("Restored Person3: " + restoredPerson2);

            //Original Person3: Person [name=打工充, age=30, address=武汉, cardId=2143324]
            //Restored Person3: Person [name=打工充, age=30, address=武汉, cardId=2143324]
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



}
