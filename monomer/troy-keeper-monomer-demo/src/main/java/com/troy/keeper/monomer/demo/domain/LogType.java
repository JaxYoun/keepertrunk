package com.troy.keeper.monomer.demo.domain;

/**
 * Created by yjm on 2017/6/8.
 */
public enum LogType {

    ADD((byte) 0, "添加"),
    UPDATE((byte) 1, "更新"),
    DELETE((byte) 2, "删除");

    private byte code;

    private String name;

    LogType(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public byte getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static LogType valueFrom(byte code){
        LogType[] values = values();
        for (LogType value : values) {
            if (value.getCode()== code) {
                return value;
            }
        }
        return null;
    }

    public static byte index(byte index) {
        int length = LogType.values().length;
        return (byte) (((index / length) + 1) * 10 + index % length);
    }
}
