package com.ineyee._02_DI.domain;

public class ThirdPartyClass {
    private Object datasource;

    public void setDatasource(Object datasource) {
        this.datasource = datasource;
    }

    @Override
    public String toString() {
        return "ThirdPartyClass{" +
                "datasource=" + datasource +
                '}';
    }
}
