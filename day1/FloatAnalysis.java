package day1;


import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 浮点数底层研究
 **/
public class FloatAnalysis{

    public static void main(String[] args) throws Exception {
        // 需要计算真实存储值的浮点数
        float floatValue1 = 16.35f;
        float floatValue2 = .35f;
        System.out.println(floatValue1 + "的真实存储值：" + rayFloatStudy(floatValue1));
        System.out.println("\n========我是一条华丽的分割线========\n");
        System.out.println(floatValue2 + "的真实存储值：" + rayFloatStudy(floatValue2));


        double f1 = FloatAnalysis.rayFloatStudy(0.1f);
        double f2 = FloatAnalysis.rayFloatStudy(0.2f);
        double f3 = FloatAnalysis.rayFloatStudy(0.3f);
        System.out.println(f1 + "\t= 0.1f");          // 0.10000000149011612	= 0.1f
        System.out.println(f2 + "\t= 0.2f");          // 0.20000000298023224	= 0.2f
        System.out.println(f3 + "\t= 0.3f");          // 0.30000001192092896	= 0.3f


        double f4 = FloatAnalysis.rayFloatStudy(1.4f);
        System.out.println(f4 + "\t= 1.4f");
        double f5 = FloatAnalysis.rayFloatStudy(1.5f);
        System.out.println(f5 + "\t= 1.5f");
    }

    /**
     * 解析浮点数组成
     *
     * @param floatValue 待解析的浮点数
     * @author LiaoYuXing-Ray 2024/1/4 10:01
     **/
    public static double rayFloatStudy(float floatValue) throws Exception {
        // 符号位(1位)
        byte[] symbol = new byte[1];
        // 阶码位(8位)，指数部分
        byte[] exponent = new byte[8];
        // 尾数位(23位)，有效数字
        byte[] number = new byte[23];

        String binaryRepresentation = floatToBinary(floatValue);

        System.out.println("浮点数\t" + floatValue);
        System.out.println("========开始解析每一位的含义========");
        System.out.println(binaryRepresentation);
        byte[] byteString = binaryRepresentation.getBytes();
        // float 4字节 1字节=8bit 所以32
        byte[] byteArray = new byte[32];
        // ASCII码中48是0 49是1
        for (int i = 0; i < byteString.length; i++) {
            if (byteString[i] == 48) {
                byteArray[i] = 0;
            } else if (byteString[i] == 49) {
                byteArray[i] = 1;
            } else {
                throw new Exception("不应该出现的分支，理论上转为二进制只有0和1");
            }
        }

        /*
            以下是获取符号位、指数部分、有效数字的值，并输出
         */
        for (int i = 0; i < byteArray.length; i++) {
            if (i == 0) {
                symbol[i] = byteArray[i];
            }
            if (i >= 1 && i <= 8) {
                exponent[i - 1] = byteArray[i];
            }
            if (i > 8) {
                number[i - 9] = byteArray[i];
            }
            // 以下为输出，可注释
            if (i != 0 && i % 4 == 0) {
                System.out.print("-");
            }
            System.out.print(byteArray[i]);
        }

        System.out.print("\n符号位值(1位)：" + Arrays.toString(symbol));
        if (symbol[0] == 0) {
            System.out.print("为正数");
        } else if (symbol[0] == 1) {
            System.out.print("为负数");
        } else {
            throw new Exception("不应该出现的分支，符号位理论上只有0和1两种情况");
        }

        System.out.print("\n指数部分(8位)：[");
        for (byte b : exponent) {
            System.out.print(b);
        }
        System.out.print("]\t-> 转化十进制数：[" + binaryToDecimal(exponent) + "]");

        System.out.print("\n有效数字(23位)：[");
        for (byte b : number) {
            System.out.print(b);
        }
        System.out.print("]");

        System.out.println("\n=======还原float的真实存储值=======");

        // 指数部分的十进制数值
        int exponentOfDecimalNumber = binaryToDecimal(exponent);
        /*
            以 IEEE754 标准规定，单精度的阶码偏移量为 2^(n-1)-1 (即127)，这样能表示的指数范围为 [-126,127]
         */
        // 阶码偏移量
        int offsetExponent = exponentOfDecimalNumber - 127;
        System.out.println("阶码偏移量=指数部分的十进制数值[" + exponentOfDecimalNumber + "]- [2^(n-1)-1 (即127)]=" + offsetExponent);

        // 指数值。此处double是因为Math.pow方法的参数为double类型
        double integerBitsBaseValue;
        if (offsetExponent >= 0) {
            integerBitsBaseValue = 1 << offsetExponent;
        } else {
            integerBitsBaseValue = Math.pow(2D, offsetExponent);
        }
        System.out.println("指数值=2^阶码偏移量[" + offsetExponent + "]=" + integerBitsBaseValue);

        // 整数部分(小数点左边的部分)即整数位(基础值)
        double integerBits = integerBitsBaseValue;

        // 有效位数转化为10进制数
        double tempCount;
        if (offsetExponent >= 0) {
            tempCount = 1D;
            // 如果偏移量大于等于0，整数部分为0
            integerBits = 0;
            System.out.println("整数部分(小数点左边的部分)即整数位(基础值)=" + integerBits);
            System.out.println("尾数23位实际为1.xxx，尾数为有效数字加上1.0");
            System.out.print("所以有效位数的二进制表示为[1.");

        } else {
            tempCount = 0D;
            System.out.println("整数部分(小数点左边的部分)即整数位(基础值)：" + integerBits);
            System.out.println("尾数23位部分为0.xxx，尾数为有效数字加上0.0");
            System.out.print("所以有效位数的二进制表示为[0.");
        }

        /*
            此处计算小数二进制转化为十进制，比如0.01(2)=0*2^(-1)+1*2^(-2)=0.25(10)
         */
        for (int i = 0; i < number.length; i++) {
            if (number[i] == 1) {
                // 将2的负(i+1)次方累加
                tempCount += Math.pow(2, -(i + 1));
            }
            System.out.print(number[i]);
        }
        System.out.println("] -> 有效位数10进制数的值" + tempCount);

        // 小数有效值
        double decimalEffectiveValue = tempCount * integerBitsBaseValue;
        System.out.println("(有效位数10进制数的值" + tempCount + ")*(指数值" + integerBitsBaseValue + ")=小数有效值：" + decimalEffectiveValue);
        double result = decimalEffectiveValue + integerBits;
        if (byteArray[0] == 0) {
            System.err.println(floatValue + "为正数，小数有效值[" + decimalEffectiveValue + "]加上整数部分[" + integerBits + "]，最终结果：" + result);
        } else {
            System.err.println(floatValue + "为负数结果需要*(-1)，小数有效值[" + decimalEffectiveValue + "]加上整数部分[" + integerBits + "]，最终结果：" + (result * -1));
        }

        // 休眠是因为防止err语句输出顺序混乱
        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 长度为8的byte[]转换为对应的十进制数
     *
     * @param exponentBytes 长度为8的byte[]
     * @return int
     * @author LiaoYuXing-Ray 2024/1/4 10:01
     **/
    private static int binaryToDecimal(byte[] exponentBytes) {
        int decimal = 0;
        for (int i = 0; i < exponentBytes.length; i++) {
            decimal += (exponentBytes[i] & 0xFF) * Math.pow(2, exponentBytes.length - 1 - i);
        }
        return decimal;
    }

    /**
     * 浮点型转化为bit，字符串输出，会自动补零
     *
     * @param value 浮点数
     * @return java.lang.String
     * @author LiaoYuXing-Ray 2024/1/4 10:01
     **/
    private static String floatToBinary(float value) {
        int intBits = Float.floatToIntBits(value);
        return String.format("%32s", Integer.toBinaryString(intBits)).replace(' ', '0');
    }
}


