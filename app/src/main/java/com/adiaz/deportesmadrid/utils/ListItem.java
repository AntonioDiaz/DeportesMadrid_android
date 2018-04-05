package com.adiaz.deportesmadrid.utils;

public class ListItem {

    String name;
    String count;

    public ListItem(String n, String i) {
        name = n;
        count = i;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
