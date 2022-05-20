package com.example.commontool.netty.nettyadv.vo;

/**
 * @ClassName MessageCode
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/19
 **/
public enum MessageCode {

    /**成功*/
    SUCCESS(20000,"成功!"),
    /**登录成功*/
    LOGIN_SUCCESS(20200,"登录成功!"),

    /**拒绝请求*/
    REJECTION_LOGIN(40100,"拒绝请求!"),

    /**重复登录*/
    DUP_LOGIN(40200,"重复登录!"),

    /**登录失败*/
    LOGIN_FAIL(40400,"登录失败!"),
    /**失败*/
    ERROR(40000,"失败!");



    private int value;

    private String msg;


    private MessageCode(int value) {
        this.value = value;
    }
    private MessageCode(int value,String msg) {
        this.value = value;
        this.msg = msg;
    }

    public int value() {
        return this.value;
    }
    public String message() {
        return this.msg;
    }

    @Override
    public String toString() {
        return "MessageCode [value=" + value
                + ", msg=" + msg + "]";
    }
}
