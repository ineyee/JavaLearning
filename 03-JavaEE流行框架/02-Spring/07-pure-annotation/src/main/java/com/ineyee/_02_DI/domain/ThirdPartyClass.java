package com.ineyee._02_DI.domain;

public class ThirdPartyClass {
    private Integer type;

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ThirdPartyClass{" +
                "type=" + type +
                '}';
    }
}
