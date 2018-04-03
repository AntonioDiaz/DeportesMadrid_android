package com.adiaz.deportesmadrid.utils;

public class RecyclerElement {

    String name;
    Integer count;

    public RecyclerElement(String n, Integer i) {
        name = n;
        count = i;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
