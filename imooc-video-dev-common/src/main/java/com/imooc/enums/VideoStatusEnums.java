package com.imooc.enums;

public enum VideoStatusEnums {

    SUCCESS(1),
    FORBID(2);

    private final int value;

    VideoStatusEnums(int i) {
        this.value = i;
    }

    public int getValue()
    {
        return value;
    }
}
