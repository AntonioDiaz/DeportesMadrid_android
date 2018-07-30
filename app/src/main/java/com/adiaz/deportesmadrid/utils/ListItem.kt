package com.adiaz.deportesmadrid.utils

import java.util.Comparator

class ListItem(var name: String, var count: String) {

    class ListItemCompartor : Comparator<ListItem> {
        override fun compare(item01: ListItem?, item02: ListItem?): Int {
            return if (item01 == null || item02 == null) {
                0
            } else item01.name.compareTo(item02.name)
        }
    }
}
