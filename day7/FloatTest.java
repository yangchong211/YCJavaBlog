package day7;

public class FloatTest {

    public static void main(String[] args) {
        test1();
        test2();
    }

    private static void test1() {
        float a = 2.0000001F;
        float b = 2.0000001F;
        System.out.println(a == b);                 // 输出true
        System.out.println(0.1f + 0.2f == 0.3f);    // 输出true
        System.out.println(0.1 + 0.2 == 0.3);       // 输出false
        System.out.println(0.3f + 0.6f == 0.9f);    // 输出false
        System.out.println(0.3 + 0.6 == 0.9);       // 输出false
        System.out.println(0.6 + 0.6 == 1.2);       // 输出true
        System.out.println(0.2 + 0.2 == 0.4);       // 输出true

        //打印结果
        //true
        //true
        //false
        //false
        //false
        //true
        //true
    }

    private static void test2() {
        float f = 1.4f;
        double d = 1.4d;
        float f1 = 1.5f;
        double d1 = 1.5d;

        System.out.println(f);              //1.4
        System.out.println((double)f);      //1.399999976158142
        System.out.println(d);              //1.4
        System.out.println(f - d);          //-2.3841857821338408E-8
        System.out.println(f == d);         //false

        System.out.println("--------");
        System.out.println(f1);             //1.5
        System.out.println((double) f1);    //1.5
        System.out.println(d1);             //1.5
        System.out.println(f1 - d1);        //0.0
        System.out.println(f1 == d1);       //true


        //1.4
        //1.399999976158142
        //1.4
        //-2.3841857821338408E-8
        //false
        //--------
        //1.5
        //1.5
        //1.5
        //0.0
        //true
    }

}

