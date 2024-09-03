package day1;

public class IntTest {

    public static void main(String[] args) {
        //装箱：将基本类型转化为包装类型
        Integer i = 10;
        //拆箱：将包装类型转化为基本类型
        int c = i;
    }


    public class Student <T>{

        private int age;

        public void setAge(T t) {

        }

    }

}
