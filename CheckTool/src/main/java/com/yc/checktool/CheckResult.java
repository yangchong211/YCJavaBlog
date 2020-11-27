package com.yc.checktool;

public class CheckResult {

    //可能是模拟器
    public static final int RESULT_MAYBE_EMULATOR = 0;
    //模拟器
    public static final int RESULT_EMULATOR = 1;
    //可能是真机
    public static final int RESULT_UNKNOWN = 2;

    public int result;
    public String value;

    public CheckResult(int result, String value) {
        this.result = result;
        this.value = value;
    }
}