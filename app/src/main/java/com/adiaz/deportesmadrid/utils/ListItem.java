package com.adiaz.deportesmadrid.utils;

import java.util.Comparator;

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

    public static class ListItemCompartor implements Comparator<ListItem> {
        @Override
        public int compare(ListItem item01, ListItem item02) {
            if (item01==null || item02==null) {
                return 0;
            }
            return item01.getName().compareTo(item02.getName());
        }
    }
}
